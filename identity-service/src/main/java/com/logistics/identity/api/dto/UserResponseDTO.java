package com.logistics.identity.api.dto;

import com.logistics.identity.domain.model.AppRole;
import lombok.*;

import java.util.Set;
import java.util.UUID;

/**
 * DTO retourné en réponse après une inscription ou consultation d'utilisateur.
 *
 * Ne contient JAMAIS le mot de passe – séparation stricte des données
 * sensibles dans les réponses API (bonne pratique de sécurité).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    /** Identifiant interne du service */
    private UUID id;

    /** Identifiant Keycloak – utile pour la liaison avec la Company */
    private String keycloakUserId;

    private String username;
    private String email;
    private boolean active;
    private Set<AppRole> roles;
}
