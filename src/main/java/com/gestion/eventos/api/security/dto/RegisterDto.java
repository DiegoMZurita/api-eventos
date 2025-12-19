package com.gestion.eventos.api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de registro de un nuevo usuario")
public class RegisterDto {

    @Schema(description = "Nombre de usuario", example = "diegomtz")
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres.")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "password123")
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    @Schema(description = "Correo electrónico", example = "diego@email.com")
    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Debe ser una dirección de correo electrónico válida.")
    private String email;

    @Schema(description = "Nombre completo del usuario", example = "Diego Martínez")
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String name;

    @Schema(description = "Roles asignados al usuario", example = "[\"ROLE_USER\"]")
    private Set<String> roles;
}