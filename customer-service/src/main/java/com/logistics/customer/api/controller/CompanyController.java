package com.logistics.customer.api.controller;

import com.logistics.customer.api.dto.CompanyResponseDTO;
import com.logistics.customer.api.dto.InternalCompanyCreateDTO;
import com.logistics.customer.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exposition des points d'accès API pour la gestion des entreprises.
 * 
 * Accès protégé par la Gateway et validé par JWT Keycloak.
 */
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Endpoint interne appelé par le microservice Identity lors de l'inscription.
     * Accessible uniquement via communication inter-services (Feign).
     */
    @PostMapping("/internal")
    public ResponseEntity<CompanyResponseDTO> createInternal(
            @RequestBody InternalCompanyCreateDTO dto) {
        CompanyResponseDTO response = companyService.createInternal(dto);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Récupère le profil de l'utilisateur métier (Company) associé au JWT.
     * Le claim "sub" contient l'ID Keycloak.
     */
    @GetMapping("/me")
    public ResponseEntity<CompanyResponseDTO> getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        CompanyResponseDTO response = companyService.getMyProfile(keycloakUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAll() {
        return ResponseEntity.ok(companyService.getAll());
    }
}
