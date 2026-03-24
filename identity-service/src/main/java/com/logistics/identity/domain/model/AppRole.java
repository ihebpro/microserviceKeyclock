package com.logistics.identity.domain.model;

/**
 * Value Object représentant les rôles métier dans la plateforme logistique.
 *
 * Ces rôles sont synchronisés avec Keycloak :
 *   - ADMIN           : accès total à toutes les ressources
 *   - WAREHOUSE_MANAGER : gère les entrepôts et opérations logistiques
 *   - CUSTOMER        : accès limité à ses propres données (Company, Contracts)
 */
public enum AppRole {
    ADMIN,
    WAREHOUSE_MANAGER,
    CUSTOMER
}
