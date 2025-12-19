package com.gestion.eventos.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "speakers")
@Schema(description = "Entidad que representa un ponente")
public class Speaker {

    @Schema(description = "ID del ponente", example = "20")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre completo del ponente", example = "Juan Pérez")
    @Column(nullable = false)
    private String name;

    @Schema(description = "Email del ponente", example = "juan.perez@email.com")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Biografía del ponente")
    private String bio;

    @Schema(description = "Eventos en los que participa el ponente")
    @ManyToMany(mappedBy = "speakers")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Event> events = new HashSet<>();
}
