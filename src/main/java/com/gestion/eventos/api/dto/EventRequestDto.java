package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Schema(description = "Detalles de la solicitud para crear/actulizar un evento")
public class EventRequestDto {
    @Schema(description = "Nombre del evento", example = "Conferencia de Spring Boot")
    @NotBlank(message = "El nombre del evento no puede estar vacío.")
    private String name;

    @Schema(description = "Fecha en la que se realizará el evento", example = "2025-03-10")
    @NotNull(message = "La fecha no puede ser nula.")
    private LocalDate date;

    @Schema(description = "Ubicación donde se realizará el evento", example = "Centro de Convenciones")
    @NotBlank(message = "La ubicación no puede estar vacía.")
    private String location;

    @Schema(description = "ID de la categoría asociada al evento", example = "10")
    @NotNull(message = "La categoría es obligatoria.")
    @NotNull(message = "La categoría es obligatoria.")
    private Long categoryId;
    @Schema(description = "Lista de IDs de los ponentes asociados al evento", example = "[20, 21]")
    private Set<Long> speakersIds;
}
