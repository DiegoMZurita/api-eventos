package com.gestion.eventos.api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de inicio de sesión")
public class LoginDto {

    @Schema(description = "Nombre de usuario", example = "diegomtz")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}