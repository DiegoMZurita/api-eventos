package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de salida que representan un ponente")
public class SpeakerResponseDto {
    @Schema(description = "ID del ponente", example = "20")
    private Long id;

    @Schema(description = "Nombre del ponente", example = "Juan Pérez")
    private String name;

    @Schema(description = "Email del ponente", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Biografía del ponente")
    private String bio;
}
