package com.logistics.customer.api.dto;

import lombok.*;

/**
 * DTO interne envoyé par Identity Service lors de la création d'une entreprise.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InternalCompanyCreateDTO {
    private String name;
    private String contactEmail;
    private String keycloakUserId;
    private String role; // Correspond à AppRole.ADMIN, CUSTOMER, etc.
}
