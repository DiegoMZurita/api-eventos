package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa una categoría de eventos")
public class CategoryDto {
    @Schema(description = "ID único de la categoría", example = "1")
    private Long id;
    @Schema(description = "Nombre de la categoría", example = "Tecnología")
    private String name;
    @Schema(description = "Descripción de la categoría", example = "Eventos relacionados con tecnología y software")
    private String description;
}
