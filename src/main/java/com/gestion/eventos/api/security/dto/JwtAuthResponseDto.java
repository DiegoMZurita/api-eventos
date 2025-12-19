package com.gestion.eventos.api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta de autenticación con JWT")
public class JwtAuthResponseDto {

    @Schema(description = "Token de acceso JWT")
    private String accessToken;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType = "Bearer";

    public JwtAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}