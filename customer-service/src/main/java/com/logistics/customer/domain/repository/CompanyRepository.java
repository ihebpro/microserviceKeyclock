package com.logistics.customer.domain.repository;

import com.logistics.customer.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux données des entreprises.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    
    /**
     * Retrouve une entreprise par l'identifiant Keycloak de son propriétaire.
     * Utilisé pour récupérer le profil de l'utilisateur connecté ("me").
     */
    Optional<Company> findByKeycloakUserId(String keycloakUserId);

    /** Vérifie si une entreprise existe déjà avec ce SIREN. */
    boolean existsBySiren(String siren);
}
