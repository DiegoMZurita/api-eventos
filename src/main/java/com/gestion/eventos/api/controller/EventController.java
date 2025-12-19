package com.gestion.eventos.api.controller;


import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.service.IEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Operaciones relacionadas con la gestión de eventos")
public class EventController {

    private final IEventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/optimized/all-details")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar eventos con detalles", description = "Obtiene una lista paginada de eventos de forma detallada. Permite filtrar por nombre de forma opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de eventos obtenido exitosamente")
    })
    public ResponseEntity<List<Event>> getAllEventsWithAllDetails() {
        List<Event> events = eventService.findAllEventsWithAllDetailsOptimized();
        return ResponseEntity.ok(events);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar eventos", description = "Obtiene una lista paginada de eventos. Permite filtrar por nombre de forma opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de eventos obtenido exitosamente")
    })
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "name")Pageable pageable
            ) {
        Page<EventResponseDto> events = eventService.findAll(name, pageable);
        return ResponseEntity.ok(events);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear un nuevo evento", description = "Crea un nuevo evento con su categoría y speakers asociados. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventRequestDto requestDto) {
        Event eventSaved = eventService.save(requestDto);
        EventResponseDto responseDto = eventMapper.toResponseDto(eventSaved);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Obtener un evento por su ID", description = "Devuelve los detalles de un evento específico por su ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Evento encontrado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Evento no encontrado")
            }
    )
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        Event event = eventService.findById(id);
        EventResponseDto responseDto = eventMapper.toResponseDto(event);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar un evento existente", description = "Actualiza los datos de un evento existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id,
                                                        @Valid @RequestBody EventRequestDto requestDto
    ){
        Event updateEvent = eventService.update(id, requestDto);
        return ResponseEntity.ok(eventMapper.toResponseDto(updateEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar un evento", description = "Elimina un evento existente identificado por su ID. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })

    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
