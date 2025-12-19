package com.gestion.eventos.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "events")
@Schema(description = "Entidad que representa un evento")
public class Event {

    @Schema(description = "ID único del evento", example = "10")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del evento", example = "Conferencia Spring Boot")
    @Column(nullable = false)
    private String name;

    @Schema(description = "Fecha del evento", example = "2025-03-10")
    @Column(nullable = false)
    private LocalDate date;

    @Schema(description = "Ubicación del evento", example = "Centro de Convenciones")
    @Column(nullable = false)
    private String location;

    @Schema(description = "Ponentes asociados al evento")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "event_speakers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "speakers_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Speaker> speakers = new HashSet<>();

    @Schema(description = "Categoría del evento")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Schema(description = "Usuarios que asistieron al evento")
    @ManyToMany(mappedBy = "attendedEvents", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> attendedUsers = new HashSet<>();

    public void addSpeaker(Speaker speaker){
        this.speakers.add(speaker);
        speaker.getEvents().add(this);
    }

    public void removeSpeaker(Speaker speaker){
        this.speakers.remove(speaker);
        speaker.getEvents().remove(this);
    }
}
