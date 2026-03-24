package com.logistics.identity.api.dto;

import com.logistics.identity.domain.model.AppRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO reçu lors de l'inscription d'une Company/utilisateur.
 *
 * Les annotations @NotBlank et @Email seront validées automatiquement
 * via @Valid dans le Controller avant d'atteindre le Service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    /**
     * Rôle attribué lors de l'inscription.
     * Par défaut CUSTOMER si non spécifié (géré dans le service).
     */
    private AppRole role;
}
