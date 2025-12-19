package com.gestion.eventos.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
@Schema(description = "Entidad que representa una categoría de eventos")
public class Category {
    @Schema(description = "ID único de la categoría", example = "1")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la categoría", example = "Tecnología")
    @Column(nullable = false, unique = true)
    private String name;

    @Schema(description = "Descripción de la categoría", example = "Eventos relacionados con tecnología y desarrollo de software")
    @Column(length = 500)
    private String description;

}
