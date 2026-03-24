package com.logistics.identity.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entité JPA représentant un utilisateur de la plateforme logistique.
 *
 * Un AppUser est une représentation locale du compte Keycloak.
 * Il est lié à Keycloak via le champ {@code keycloakUserId} qui correspond
 * au claim "sub" du JWT.
 *
 * Un utilisateur peut être associé à une Company dans le Customer Service
 * (lien logique via keycloakUserId, pas de clé étrangère JPA).
 */
@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Identifiant unique de l'utilisateur dans Keycloak.
     * Correspond au claim "sub" dans le JWT.
     * Utilisé pour retrouver la Company associée dans Customer Service.
     */
    @Column(nullable = false, unique = true)
    private String keycloakUserId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    /** Indique si le compte est activé dans le système. */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    /**
     * Rôles métier de l'utilisateur dans la plateforme.
     * EAGER car les rôles sont souvent requis immédiatement avec l'utilisateur.
     * Stockés via une table de jointure @ElementCollection.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Set<AppRole> roles = new HashSet<>();
}
