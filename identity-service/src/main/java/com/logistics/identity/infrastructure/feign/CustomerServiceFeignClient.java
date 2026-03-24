package com.logistics.identity.infrastructure.feign;

import com.logistics.identity.api.dto.feign.CreateCompanyFeignDTO;
import com.logistics.identity.api.dto.feign.CompanyFeignResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Client Feign vers le Customer Service.
 *
 * Après l'inscription d'un utilisateur (création dans Keycloak + AppUser local),
 * l'Identity Service crée automatiquement la Company associée dans
 * le Customer Service via cet appel Feign.
 *
 * Le load-balancing via Consul (lb://customer-service) est géré automatiquement
 * par Spring Cloud LoadBalancer + Consul Discovery.
 */
@FeignClient(name = "customer-service", path = "/api/v1")
public interface CustomerServiceFeignClient {

    /**
     * Crée une Company dans le Customer Service lors de l'inscription.
     *
     * @param authHeader  Token JWT valide à transmettre au Customer Service
     * @param companyDTO  Données de la Company à créer
     * @return CompanyFeignResponseDTO informations de la Company créée
     */
    @PostMapping("/companies/internal")
    CompanyFeignResponseDTO createCompany(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCompanyFeignDTO companyDTO
    );
}
