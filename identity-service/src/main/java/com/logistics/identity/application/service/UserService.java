package com.logistics.identity.application.service;

import com.logistics.identity.api.dto.RegisterRequestDTO;
import com.logistics.identity.api.dto.UserResponseDTO;
import com.logistics.identity.api.dto.LoginRequestDTO;
import com.logistics.identity.api.dto.LoginResponseDTO;
import com.logistics.identity.api.dto.feign.CompanyFeignResponseDTO;
import com.logistics.identity.api.dto.feign.CreateCompanyFeignDTO;
import com.logistics.identity.domain.model.AppRole;
import com.logistics.identity.domain.model.AppUser;
import com.logistics.identity.domain.repository.AppUserRepository;
import com.logistics.identity.infrastructure.feign.CustomerServiceFeignClient;
import com.logistics.identity.infrastructure.keycloak.KeycloakAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

/**
 * Service métier gérant l'inscription et la consultation des utilisateurs.
 *
 * Orchestre le flux d'inscription en deux étapes atomiques :
 *   1. Crée le compte dans Keycloak (authentication)
 *   2. Sauvegarde AppUser localement + notifie le Customer Service (profil métier)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final AppUserRepository userRepository;
    private final KeycloakAdminClient keycloakAdminClient;
    private final CustomerServiceFeignClient customerServiceFeignClient;

    /**
     * Enregistre un nouvel utilisateur dans la plateforme.
     *
     * Étape 1 : Crée le compte Keycloak via l'Admin API → retourne keycloakUserId
     * Étape 2 : Sauvegarde un AppUser local avec le keycloakUserId
     * Étape 3 : Appelle le Customer Service via Feign pour créer la Company liée
     *
     * @param request DTO contenant username, email, password, role
     * @return UserResponseDTO avec le keycloakUserId et les informations créées
     * @throws IllegalStateException si l'email existe déjà en base
     */
    public UserResponseDTO register(RegisterRequestDTO request) {
        // ── Vérification d'unicité ──────────────────────────────────────────────
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Un compte avec cet email existe déjà : " + request.getEmail());
        }

        // ── Étape 1 : Créer l'utilisateur dans Keycloak ─────────────────────────
        log.info("Création du compte Keycloak pour : {}", request.getEmail());
        String keycloakUserId = keycloakAdminClient.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        // ── Étape 2 : Persister localement ─────────────────────────────────────
        AppRole role = (request.getRole() != null) ? request.getRole() : AppRole.CUSTOMER;
        AppUser appUser = AppUser.builder()
                .keycloakUserId(keycloakUserId)
                .username(request.getUsername())
                .email(request.getEmail())
                .roles(Set.of(role))
                .active(true)
                .build();
        AppUser savedUser = userRepository.save(appUser);
        log.info("AppUser sauvegardé localement avec ID: {}", savedUser.getId());

        // ── Étape 3 : Créer la Company dans Customer Service via Feign ──────────
        // Note : En cas d'échec Feign, Resilience4j peut déclencher le circuit breaker
        CreateCompanyFeignDTO companyDTO = CreateCompanyFeignDTO.builder()
                .keycloakUserId(keycloakUserId)
                .email(request.getEmail())
                .role(role)
                .build();

        try {
            CompanyFeignResponseDTO company = customerServiceFeignClient
                    .createCompany("internal-call", companyDTO);
            log.info("Company créée dans Customer Service: {}", company.getId());
        } catch (Exception e) {
            // On logue l'erreur mais on ne bloque pas l'inscription
            // La Company pourra être créée manuellement si nécessaire
            log.error("Impossible de créer la Company dans Customer Service: {}", e.getMessage());
        }

        return toResponseDTO(savedUser);
    }

    /**
     * Authentifie un utilisateur et retourne ses tokens de session.
     *
     * @param request DTO contenant credentials (username/password)
     * @return LoginResponseDTO contenant access_token, refresh_token, etc.
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Tentative de connexion pour l'utilisateur : {}", request.getUsername());
        Map<String, Object> tokenData = keycloakAdminClient.authenticateUser(
                request.getUsername(),
                request.getPassword()
        );

        return LoginResponseDTO.builder()
                .accessToken((String) tokenData.get("access_token"))
                .expiresIn((Integer) tokenData.get("expires_in"))
                .refreshExpiresIn((Integer) tokenData.get("refresh_expires_in"))
                .refreshToken((String) tokenData.get("refresh_token"))
                .tokenType((String) tokenData.get("token_type"))
                .idToken((String) tokenData.get("id_token"))
                .sessionState((String) tokenData.get("session_state"))
                .scope((String) tokenData.get("scope"))
                .build();
    }

    /**
     * Convertit un AppUser (entité JPA) en UserResponseDTO (objet API).
     * Cette méthode de mapping évite d'exposer les détails internes de l'entité.
     *
     * @param user Entité AppUser à convertir
     * @return UserResponseDTO
     */
    private UserResponseDTO toResponseDTO(AppUser user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .keycloakUserId(user.getKeycloakUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .roles(user.getRoles())
                .build();
    }
}
