package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de salida que representan un usuario del sistema")
public class UserResponseDto {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Diego Martínez")
    private String name;

    @Schema(description = "Nombre de usuario", example = "diegomtz")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "diego@email.com")
    private String email;

    @Schema(description = "Roles asignados al usuario")
    private List<RoleDto> roles;

    @Schema(description = "Eventos a los que el usuario ha asistido")
    private List<EventSummaryDto> attendedEvents;
}
