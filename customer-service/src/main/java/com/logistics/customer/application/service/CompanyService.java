package com.logistics.customer.application.service;

import com.logistics.customer.api.dto.CompanyResponseDTO;
import com.logistics.customer.api.dto.InternalCompanyCreateDTO;
import com.logistics.customer.domain.model.Company;
import com.logistics.customer.domain.model.CustomerType;
import com.logistics.customer.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Orchestrateur de la logique métier liée aux entreprises.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * Méthode interne appelée lors de l'inscription via Identity Service.
     * Initialise le profil de l'entreprise.
     */
    public CompanyResponseDTO createInternal(InternalCompanyCreateDTO dto) {
        log.info("Initialisation interne de la Company pour Keycloak ID: {}", dto.getKeycloakUserId());

        // Mapping basique (SIREN et Adresse seront complétés plus tard par l'utilisateur connecté)
        Company company = Company.builder()
                .name(dto.getName())
                .contactEmail(dto.getContactEmail())
                .keycloakUserId(dto.getKeycloakUserId())
                // Déduction basique du type de client selon le rôle Identity
                .customerType(dto.getRole().equals("ADMIN") ? CustomerType.PRESTATAIRE : CustomerType.CLIENT)
                .siren("PENDING_" + dto.getKeycloakUserId()) // Placeholder
                .build();

        Company saved = companyRepository.save(company);
        return toResponseDTO(saved);
    }

    /** Retrouve le profil de l'utilisateur connecté via son JWT (claim "sub"). */
    @Transactional(readOnly = true)
    public CompanyResponseDTO getMyProfile(String keycloakUserId) {
        return companyRepository.findByKeycloakUserId(keycloakUserId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Profil entreprise introuvable pour ce compte."));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDTO> getAll() {
        return companyRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private CompanyResponseDTO toResponseDTO(Company company) {
        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .siren(company.getSiren())
                .contactEmail(company.getContactEmail())
                .customerType(company.getCustomerType().name())
                .keycloakUserId(company.getKeycloakUserId())
                .build();
    }
}
