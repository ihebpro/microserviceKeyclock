package com.logistics.customer.application.service;

import com.logistics.customer.api.dto.UserResponseDTO;
import com.logistics.customer.api.dto.UserCreateDTO;
import com.logistics.customer.domain.model.User;
import com.logistics.customer.domain.model.UserRole;
import com.logistics.customer.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createInternal(UserCreateDTO dto) {
        log.info("Creation interne d'un User : {}", dto.getEmail());

        UserRole type = UserRole.SELLER;
        try {
            if (dto.getRole() != null) {
                type = UserRole.valueOf(dto.getRole().toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Role {} was not found in UserRole, defaulting to SELLER", dto.getRole());
        }

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .keycloakUserId(dto.getKeycloakUserId())
                .role(type)
                .warehouseMode(false)
                .build();

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getMyProfile(String email) {
        return userRepository.findByEmail(email)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Profil introuvable pour ce compte."));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .warehouseMode(user.isWarehouseMode())
                .keycloakUserId(user.getKeycloakUserId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
