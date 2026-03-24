package com.logistics.customer.api.dto;

import lombok.*;
import java.util.UUID;

/**
 * Objet de transfert de données pour l'exposition d'une entreprise via l'API.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompanyResponseDTO {
    private UUID id;
    private String name;
    private String siren;
    private String contactEmail;
    private String customerType;
    private String keycloakUserId;
}
