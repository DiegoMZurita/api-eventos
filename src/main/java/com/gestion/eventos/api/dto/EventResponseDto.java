package com.gestion.eventos.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EventResponseDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private CategoryDto category;
    private List<SpeakerResponseDto> speakers;
}
