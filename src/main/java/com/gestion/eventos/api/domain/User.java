package com.gestion.eventos.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@Schema(description = "Entidad que representa un usuario del sistema")
public class User {

    @Schema(description = "ID del usuario", example = "1")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre completo del usuario", example = "Diego Martínez")
    private String name;

    @Schema(description = "Nombre de usuario", example = "diegomtz")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "diego@email.com")
    private String email;

    @Schema(description = "Contraseña encriptada del usuario")
    private String password;

    @Schema(description = "Roles asignados al usuario")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @Schema(description = "Eventos a los que el usuario ha asistido")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_attended_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<Event> attendedEvents = new HashSet<>();

    public void addAttendedEvent(Event event) {
        this.attendedEvents.add(event);
        event.getAttendedUsers().add(this);
    }

    public void removeAttendedEvent(Event event) {
        this.attendedEvents.remove(event);
        event.getAttendedUsers().remove(this);
    }
}
