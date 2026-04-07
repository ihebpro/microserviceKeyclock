package com.logistics.identity.infrastructure.feign;

import com.logistics.identity.api.dto.feign.UserCreateFeignDTO;
import com.logistics.identity.api.dto.feign.UserFeignResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer-service")
public interface CustomerServiceFeignClient {

    @PostMapping("/api/v1/users/internal")
    UserFeignResponseDTO createUser(@RequestBody UserCreateFeignDTO userDTO);
}
