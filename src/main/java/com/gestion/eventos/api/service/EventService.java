package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Evento no encontrado con id: " + id)
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Event eventToDelete = this.findById(id);
        eventRepository.delete(eventToDelete);
    }
}
