package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    List<EventResponseDto> toEventResponseDtoList(List<Event> events);
    Event toEntity(EventRequestDto eventRequestDto);
    EventResponseDto toResponseDto(Event event);
    void updateEventFromDto(EventRequestDto dto, @MappingTarget Event event);
}
