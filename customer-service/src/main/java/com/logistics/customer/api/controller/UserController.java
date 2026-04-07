package com.logistics.customer.api.controller;

import com.logistics.customer.api.dto.UserResponseDTO;
import com.logistics.customer.api.dto.UserCreateDTO;
import com.logistics.customer.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/internal")
    public ResponseEntity<UserResponseDTO> createInternal(
            @RequestBody UserCreateDTO dto) {
        UserResponseDTO response = userService.createInternal(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(
            @RequestHeader("X-User-Email") String email) {
        UserResponseDTO response = userService.getMyProfile(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}
