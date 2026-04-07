package com.logistics.identity.api.dto.feign;

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
public class UserFeignResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String role;
    private boolean warehouseMode;
    private LocalDateTime createdAt;
}
