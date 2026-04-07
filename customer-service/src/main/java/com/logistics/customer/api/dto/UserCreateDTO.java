package com.logistics.customer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String keycloakUserId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String role;
}
