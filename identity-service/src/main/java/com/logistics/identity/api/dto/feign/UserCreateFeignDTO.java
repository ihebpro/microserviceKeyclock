package com.logistics.identity.api.dto.feign;

import com.logistics.identity.domain.model.AppRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateFeignDTO {
    private String keycloakUserId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private AppRole role;
}
