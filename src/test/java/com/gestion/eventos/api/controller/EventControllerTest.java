package com.gestion.eventos.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    @Test
    @DisplayName("GET /api/v1/events/{id} - Debe retornar 404 Not Found cuando el evento no existe")
    @WithMockUser(username = "testUsert", roles = "USER")
    void shouldReturnNotFoundWhenEventDoesNotExist() throws Exception{
        when(eventService.findById(anyLong())).thenThrow(
                new ResourceNotFoundException("Evento no encontrado con id: 99")
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events/{id}", 99L)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Evento no encontrado con id: 99"));

        verify(eventService, times(1)).findById(99L);
        verify(eventMapper, never()).toResponseDto(any(Event.class));


    }

    @Test
    @DisplayName("GET /api/v1/events - Debe retornar todos los eventos paginados y filtrados")
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnAllEventsPagedAndFiltered() throws Exception {

        //Preparación
        SpeakerResponseDto speakerResponseA = new SpeakerResponseDto(20L, "Juan Pérez", "juan.perez@example.com", "Experto en Spring Boot.");
        Set<SpeakerResponseDto> speakersA = new HashSet<>(Set.of(speakerResponseA));

        EventResponseDto eventResponse2 = new EventResponseDto();
        eventResponse2.setId(2L);
        eventResponse2.setName("Webinar de Spring Security");
        eventResponse2.setDate(LocalDate.of(2024, 12, 1));
        eventResponse2.setLocation("Virtual");
        eventResponse2.setCategoryName("Tecnología");
        eventResponse2.setCategoryId(10L);
        eventResponse2.setSpeakers(speakersA); // Asigna los speakers

        EventResponseDto eventResponse3 = new EventResponseDto();
        eventResponse3.setId(3L);
        eventResponse3.setName("Conferencia Cloud Nativo");
        eventResponse3.setDate(LocalDate.of(2025, 1, 20));
        eventResponse3.setLocation("Auditorio Principal");
        eventResponse3.setCategoryName("Tecnología");
        eventResponse3.setCategoryId(10L);
        eventResponse3.setSpeakers(speakersA); // Asigna los mismos speakers

        List<EventResponseDto> eventResponseList = List.of(eventResponse2, eventResponse3);

        Pageable pageableMock = PageRequest.of(0, 10);

        Page<EventResponseDto> eventResponseDtoPage = new PageImpl<>(eventResponseList,
                pageableMock, eventResponseList.size());

        when(eventService.findAll(eq("Spring"), any(Pageable.class))).thenReturn(eventResponseDtoPage);

        //Ejecución
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events")
                        .param("page", "0")
                        .param("size", "10")
                        .param("name", "Spring")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                //Verificación
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0]").exists())
                .andExpect(jsonPath("$.content[1]").exists())
                .andExpect(jsonPath("$.content[2]").doesNotExist())

                // Verificar los datos del primer evento en la página
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Webinar de Spring Security"))
                .andExpect(jsonPath("$.content[0].date").value("2024-12-01"))
                .andExpect(jsonPath("$.content[0].location").value("Virtual"))
                .andExpect(jsonPath("$.content[0].categoryName").value("Tecnología"))
                .andExpect(jsonPath("$.content[0].speakers.length()").value(1))
                .andExpect(jsonPath("$.content[0].speakers[?(@.name == 'Juan Pérez')].id").value(20))
                .andExpect(jsonPath("$.content[0].speakers[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
                .andExpect(jsonPath("$.content[0].speakers[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
                .andExpect(jsonPath("$.content[0].speakers[?(@.name == 'Juan Pérez')].bio").value("Experto en Spring Boot."))

                // Verificar los datos del segundo evento en la página
                .andExpect(jsonPath("$.content[1].id").value(3))
                .andExpect(jsonPath("$.content[1].name").value("Conferencia Cloud Nativo"))
                .andExpect(jsonPath("$.content[1].date").value("2025-01-20"))
                .andExpect(jsonPath("$.content[1].location").value("Auditorio Principal"))
                .andExpect(jsonPath("$.content[1].categoryName").value("Tecnología"))
                .andExpect(jsonPath("$.content[1].speakers.length()").value(1))
                .andExpect(jsonPath("$.content[1].speakers[?(@.name == 'Juan Pérez')].id").value(20))
                .andExpect(jsonPath("$.content[1].speakers[?(@.name == 'Juan Pérez')].name").value("Juan Pérez"))
                .andExpect(jsonPath("$.content[1].speakers[?(@.name == 'Juan Pérez')].email").value("juan.perez@example.com"))
                .andExpect(jsonPath("$.content[1].speakers[?(@.name == 'Juan Pérez')].bio").value("Experto en Spring Boot."))

                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));


        verify(eventService, times(1)).findAll(eq("Spring"), any(Pageable.class));
        verify(eventService, never()).findById(anyLong());
    }


}