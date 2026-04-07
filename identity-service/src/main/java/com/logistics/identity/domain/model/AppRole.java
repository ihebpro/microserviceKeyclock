package com.logistics.identity.domain.model;

/**
 * Value Object représentant les rôles métier dans la plateforme logistique.
 *
 * Ces rôles sont synchronisés avec Keycloak :
 *   - ADMIN     : Accès total
 *   - SELLER    : Vendeur (source de livraison)
 *   - WAREHOUSE : Entrepôt (lieu de stockage et préparation)
 *   - DRIVER    : Livreur (transport des colis)
 */
public enum AppRole {
    ADMIN,
    SELLER,
    WAREHOUSE,
    DRIVER
}
