package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Schema(description = "Datos de salida que representan un evento")
public class EventResponseDto {
    @Schema(description = "ID del evento", example = "5")
    private Long id;

    @Schema(description = "Nombre del evento", example = "Taller de Microservicios")
    private String name;

    @Schema(description = "Fecha del evento", example = "2025-03-10")
    private LocalDate date;

    @Schema(description = "Ubicación del evento", example = "Centro de Convenciones")
    private String location;

    @Schema(description = "ID de la categoría del evento", example = "10")
    private Long categoryId;

    @Schema(description = "Nombre de la categoría", example = "Tecnología")
    private String categoryName;

    @Schema(description = "Lista de ponentes asociados al evento")
    private Set<SpeakerResponseDto> speakers;
}
