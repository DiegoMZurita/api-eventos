package com.gestion.eventos.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.security.jwt.JwtAuthEntryPoint;
import com.gestion.eventos.api.security.jwt.JwtAuthenticationFilter;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import com.gestion.eventos.api.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = EventController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class // Excluye la configuración de seguridad principal y UserDetailsService
        },
        // Añadimos excludeFilters para evitar que Spring escanee y cree tus beans de seguridad específicos
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                JwtAuthenticationFilter.class,
                JwtGenerator.class,
                JwtAuthEntryPoint.class // Si JwtAuthEntryPoint también es un @Component y causa problemas
        })
)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private EventService eventService;
    private EventMapper eventMapper;


    private ObjectMapper objectMapper;

    private EventResponseDto eventResponseDto;
    private Event eventEntity;

    @TestConfiguration
    static class EventControllerTestConfig{
        @Bean
        @Primary
        EventService eventService(){
            return mock(EventService.class);
        }

        @Bean
        @Primary
        EventMapper eventMapper(){
            return mock(EventMapper.class);
        }
    }

    @BeforeEach
    void setUp(@Autowired EventService eventServiceMock, @Autowired EventMapper eventMapperMock){
        this.eventService = eventServiceMock;
        this.eventMapper = eventMapperMock;

        reset(eventService, eventMapper);

        Category category = new Category(10L, "Tecnología", "Eventos tecnológicos");
        Speaker speaker1 = new Speaker(20L, "Juan Pérez", "juan.perez@example.com", "Experto en Spring Boot.", new HashSet<>());
        Speaker speaker2 = new Speaker(21L, "María García", "maria.garcia@example.com", "Arquitecta de software.", new HashSet<>());

        eventEntity = new Event();
        eventEntity.setId(1L);
        eventEntity.setName("Conferencia Tech");
        eventEntity.setDate(LocalDate.of(2024, 11, 15));
        eventEntity.setLocation("Online");
        eventEntity.setCategory(category);
        eventEntity.addSpeaker(speaker1);
        eventEntity.addSpeaker(speaker2);

        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName("Conferencia Tech");
        eventResponseDto.setDate(LocalDate.of(2024, 11, 15));
        eventResponseDto.setLocation("Online");
        eventResponseDto.setCategoryName("Tecnología");
        eventResponseDto.setCategoryId(10L);

        // DTOs de Speakers para el EventResponseDto
        SpeakerResponseDto speakerResponse1 = new SpeakerResponseDto(20L, "Juan Pérez", "juan.perez@example.com", "Experto en Spring Boot.");
        SpeakerResponseDto speakerResponse2 = new SpeakerResponseDto(21L, "María García", "maria.garcia@example.com", "Arquitecta de software.");

        Set<SpeakerResponseDto> speakersDto = new HashSet<>();
        speakersDto.add(speakerResponse1);
        speakersDto.add(speakerResponse2);
        eventResponseDto.setSpeakers(speakersDto);

    }

    @Test
    @DisplayName("GET /api/v1/events/{id} - Debe retornar un evento por ID cuando existe")
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnEventById() throws Exception{

        //Preparación
        when(eventService.findById(anyLong())).thenReturn(eventEntity);

        when(eventMapper.toResponseDto(any(Event.class))).thenReturn(eventResponseDto);

        //Ejecución
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))

                //Verificación
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Conferencia Tech"))
                .andExpect(jsonPath("$.location").value("Online"))
                .andExpect(jsonPath("$.categoryName").value("Tecnología"))

                .andExpect(jsonPath("$.speakers.length()").value(2)) // Verifica que hay exactamente 2 speakers
                .andExpect(jsonPath("$.speakers[2]").doesNotExist())
                // --- Verificación completa de Juan Pérez (sin asumir si es [0] o [1]) ---
                .andExpect(jsonPath("$.speakers[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
                .andExpect(jsonPath("$.speakers[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
                .andExpect(jsonPath("$.speakers[?(@.name == 'Juan Pérez')].bio").value("Experto en Spring Boot."))

                // --- Verificación completa de María García (sin asumir si es [0] o [1]) ---
                .andExpect(jsonPath("$.speakers[?(@.name == 'María García')].name").value("María García"))
                .andExpect(jsonPath("$.speakers[?(@.name == 'María García')].email").value("maria.garcia@example.com"))
                .andExpect(jsonPath("$.speakers[?(@.name == 'María García')].bio").value("Arquitecta de software."));

        verify(eventService, times(1)).findById(1L);
        verify(eventMapper, times(1)).toResponseDto(eventEntity);

    }

}