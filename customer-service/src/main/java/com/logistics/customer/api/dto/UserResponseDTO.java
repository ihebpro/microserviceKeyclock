package com.logistics.customer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String role;
    private boolean warehouseMode;
    private String keycloakUserId;
    private LocalDateTime createdAt;
}
