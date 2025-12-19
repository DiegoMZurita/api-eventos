package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resumen de un evento asociado a un usuario")
public class EventSummaryDto {
    @Schema(description = "ID del evento", example = "3")
    private Long id;

    @Schema(description = "Nombre del evento", example = "Conferencia Cloud Nativo")
    private String name;

    @Schema(description = "Fecha del evento", example = "2025-01-20")
    private LocalDate date;

    @Schema(description = "Ubicación del evento", example = "Auditorio Principal")
    private String location;
}
