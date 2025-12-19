package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos de entrada para crear o actualizar un ponente")
public class SpeakerRequestDto {
    @Schema(description = "Nombre completo del ponente", example = "Juan Pérez")
    @NotBlank(message = "El nombre del ponente no puede estar vacío.")
    @Size(max = 100, message = "El nombre del ponente no puede exceder los 100 caracteres.")
    private String name;

    @Schema(description = "Correo electrónico del ponente", example = "juan.perez@example.com")
    @NotBlank(message = "El email del ponente no puede estar vacío")
    @Email(message = "El formato del email no es valido.")
    @Size(max = 100, message = "El email del ponente no puede exceder los 100 caracteres.")
    private String email;

    @Schema(description = "Biografía del ponente", example = "Experto en Spring Boot y arquitecturas backend.")
    @Size(max = 500, message = "La biografía no puede exceder los 500 caracteres.")
    private String bio;
}
