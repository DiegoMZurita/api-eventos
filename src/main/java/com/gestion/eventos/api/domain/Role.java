package com.gestion.eventos.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
@Schema(description = "Entidad que representa un rol de usuario")
public class Role {
    @Schema(description = "ID del rol", example = "1")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del rol", example = "ROLE_ADMIN")
    private String name;
}
