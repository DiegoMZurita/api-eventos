package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private ICategoryService categoryService;
    @Mock
    private ISpeakerService speakerService;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventRequestDto eventRequestDto;
    private EventResponseDto eventResponseDto;
    private Category  category;
    private Speaker speaker1;
    private Speaker speaker2;
    private Pageable pageable;

    @BeforeEach
    void setUp(){
        // Inicializar datos de prueba
        category = new Category(1L, "Conferencia", "Descripción de conferencia");
        speaker1 = new Speaker(10L, "John Doe", "john@example.com", "Bio de John", new HashSet<>());
        speaker2 = new Speaker(11L, "Jane Smith", "jane@example.com", "Bio de Jane", new HashSet<>());

        event = new Event();
        event.setId(1L);
        event.setName("Spring Boot Conf");
        event.setDate(LocalDate.of(2023, 10, 26));
        event.setLocation("Online");
        event.setCategory(category);
        event.getSpeakers().add(speaker1);
        event.getSpeakers().add(speaker2);

        eventRequestDto = new EventRequestDto();
        eventRequestDto.setName("Spring Boot Conf");
        eventRequestDto.setDate(LocalDate.of(2023, 10, 26));
        eventRequestDto.setLocation("Online");
        eventRequestDto.setCategoryId(1L);
        eventRequestDto.setSpeakersIds(new HashSet<>(Set.of(10L, 11L)));

        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName("Spring Boot Conf");
        eventResponseDto.setDate(LocalDate.of(2023, 10, 26));
        eventResponseDto.setLocation("Online");
        eventResponseDto.setCategoryId(1L);
        eventResponseDto.setCategoryName("Conferencia");


        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Debe retornar un Evento cuando el ID existe")
    void shouldReturnEventWhenIdExists(){
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        Event foundEvent = eventService.findById(1L);

        assertNotNull(foundEvent);
        assertEquals(event.getId(), foundEvent.getId());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("De Lanzar ResourceNotFoundException cuando el ID no existe")
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class, () -> {
                    eventService.findById(99L);
                });

        assertEquals("Evento no encontrado con id: 99", thrown.getMessage());

        verify(eventRepository, times(1)).findById(99L);
    }

}