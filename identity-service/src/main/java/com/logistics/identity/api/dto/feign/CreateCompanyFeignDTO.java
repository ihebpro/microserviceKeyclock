package com.logistics.identity.api.dto.feign;

import com.logistics.identity.domain.model.AppRole;
import lombok.*;

/**
 * DTO envoyé au Customer Service via Feign lors de la création d'une Company.
 * Contient les informations minimales pour créer le profil Company.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompanyFeignDTO {

    /** UUID Keycloak de l'utilisateur propriétaire (claim "sub") */
    private String keycloakUserId;

    private String email;
    private AppRole role;
}
