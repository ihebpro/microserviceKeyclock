package com.logistics.identity.api.dto;

import com.logistics.identity.domain.model.AppRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String keycloakUserId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private AppRole role;
    private boolean active;
    private boolean warehouseMode;
}
