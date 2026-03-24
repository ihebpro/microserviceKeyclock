package com.logistics.identity.api.controller;

import com.logistics.identity.api.dto.LoginRequestDTO;
import com.logistics.identity.api.dto.LoginResponseDTO;
import com.logistics.identity.api.dto.RegisterRequestDTO;
import com.logistics.identity.api.dto.UserResponseDTO;
import com.logistics.identity.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour les opérations d'authentification et d'inscription.
 *
 * Routes exposées (accessibles sans JWT – endpoints publics) :
 *   POST /api/v1/auth/register → Inscription d'une nouvelle Company/utilisateur
 *   POST /api/v1/auth/login    → Authentification pour obtenir un JWT
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Endpoint d'inscription publique.
     *
     * @param request DTO d'inscription validé automatiquement
     * @return 201 Created avec UserResponseDTO
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserResponseDTO response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint de connexion publique.
     *
     * @param request DTO de connexion contenant les identifiants
     * @return 200 OK avec LoginResponseDTO (les tokens JWT)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
