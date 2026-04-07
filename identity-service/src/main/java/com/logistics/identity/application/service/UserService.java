package com.logistics.identity.application.service;

import com.logistics.identity.api.dto.RegisterRequestDTO;
import com.logistics.identity.api.dto.UserResponseDTO;
import com.logistics.identity.api.dto.LoginRequestDTO;
import com.logistics.identity.api.dto.LoginResponseDTO;
import com.logistics.identity.api.dto.feign.UserFeignResponseDTO;
import com.logistics.identity.api.dto.feign.UserCreateFeignDTO;
import com.logistics.identity.domain.model.AppRole;
import com.logistics.identity.infrastructure.feign.CustomerServiceFeignClient;
import com.logistics.identity.infrastructure.keycloak.KeycloakAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service Stateless orchestrant l'identité.
 * 1. Crée le compte technique dans Keycloak (Auth).
 * 2. Assigne le rôle dans Keycloak.
 * 3. Notifie le customer-service pour créer le profil métier.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final KeycloakAdminClient keycloakAdminClient;
    private final CustomerServiceFeignClient customerServiceFeignClient;

    public UserResponseDTO register(RegisterRequestDTO request) {
        log.info("Inscription demandée pour : {}", request.getEmail());

        // 1. Créer l'utilisateur dans Keycloak
        String userName = request.getUserName() != null ? request.getUserName() : request.getEmail();
        String keycloakUserId = keycloakAdminClient.createUser(
                userName,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword()
        );

        // 2. Assigner le rôle dans Keycloak (RBAC)
        AppRole role = (request.getRole() != null) ? request.getRole() : AppRole.SELLER;
        keycloakAdminClient.assignRoleToUser(keycloakUserId, role.name());

        // 3. Créer le profil métier dans Customer Service via Feign
        UserCreateFeignDTO feignDTO = UserCreateFeignDTO.builder()
                .keycloakUserId(keycloakUserId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .build();

        try {
            UserFeignResponseDTO customerProfile = customerServiceFeignClient.createUser(feignDTO);
            log.info("Profil métier créé avec ID: {}", customerProfile.getId());
        } catch (Exception e) {
            log.error("Erreur lors de la création du profil métier : {}", e.getMessage());
        }

        return UserResponseDTO.builder()
                .keycloakUserId(keycloakUserId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .active(true)
                .build();
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Connexion demandée pour : {}", request.getUsername());
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
}
