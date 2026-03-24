package com.logistics.identity.domain.repository;

import com.logistics.identity.domain.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository Spring Data JPA pour l'entité AppUser.
 *
 * Spring Data génère automatiquement l'implémentation SQL de toutes les méthodes
 * déclarées ici selon le nommage des méthodes (Query DSL).
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    /**
     * Recherche un utilisateur par son identifiant Keycloak (claim "sub" du JWT).
     * Utilisé lors de la récupération du profil connecté.
     *
     * @param keycloakUserId UUID Keycloak de l'utilisateur
     * @return Optional<AppUser> vide si non trouvé
     */
    Optional<AppUser> findByKeycloakUserId(String keycloakUserId);

    /**
     * Recherche un utilisateur par son email.
     * Utilisé pour vérifier l'unicité lors de l'inscription.
     *
     * @param email Adresse email de l'utilisateur
     * @return Optional<AppUser>
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec cet email.
     * Plus performant que findByEmail() car évite le SELECT complet.
     *
     * @param email Adresse email à vérifier
     * @return true si l'email existe déjà
     */
    boolean existsByEmail(String email);
}
