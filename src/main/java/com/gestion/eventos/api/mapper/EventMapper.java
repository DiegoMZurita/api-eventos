package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.dto.EventSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    //Mapeo para la entrada - Request DTO
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "speakers", ignore = true)
    @Mapping(target = "attendedUsers", ignore = true)
    Event toEntity(EventRequestDto eventRequestDto);
    //Mapeo para salida - Response DTO
    EventResponseDto toResponseDto(Event event);
    List<EventResponseDto> toEventResponseDtoList(List<Event> events);

    //Método para Actualizar una entidad existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "speakers", ignore = true)
    @Mapping(target = "attendedUsers", ignore = true)
    void updateEventFromDto(EventRequestDto dto, @MappingTarget Event event);

    EventSummaryDto toSummaryDto(Event event);
    List<EventSummaryDto> toSummaryDtoList(List<Event> events);
}
