package com.logistics.identity.api.dto.feign;

import lombok.*;
import java.util.UUID;

/**
 * DTO retourné par le Customer Service après création d'une Company.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyFeignResponseDTO {
    private UUID id;
    private String keycloakUserId;
}
