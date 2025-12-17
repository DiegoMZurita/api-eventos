package com.gestion.eventos.api.service;


import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;

import java.util.List;

public interface IEventService {
    List<Event> findAll();
    Event save(EventRequestDto requestDto);
    Event findById(Long id);
    Event update(Long id, EventRequestDto requestDto);
    void deleteById(Long id);
}
