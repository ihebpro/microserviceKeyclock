package com.logistics.identity.api.dto;

import com.logistics.identity.domain.model.AppRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private AppRole role;
}
