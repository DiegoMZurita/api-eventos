package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.service.ISpeakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
@RequiredArgsConstructor
@Tag(name = "Speakers", description = "Operaciones relacionadas con la gestión de ponentes")
public class SpeakerController {

    private final ISpeakerService speakerService;
    private final SpeakerMapper speakerMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear un speaker", description = "Crea un nuevo ponente en el sistema. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Speaker creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<SpeakerResponseDto> createSpeaker(@Valid @RequestBody SpeakerRequestDto requestDto) {
        Speaker speaker = speakerService.save(requestDto);
        return new ResponseEntity<>(speakerMapper.toDto(speaker), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Obtener un speaker por ID", description = "Devuelve los datos de un speaker específico según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speaker encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Speaker no encontrado")
    })
    public ResponseEntity<SpeakerResponseDto> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerService.findById(id);
        return ResponseEntity.ok(speakerMapper.toDto(speaker));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar speakers", description = "Obtiene la lista completa de speakers registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de speakers obtenido exitosamente")
    })
    public ResponseEntity<List<SpeakerResponseDto>> getAllSpeakers() {
        List<Speaker> speakers = speakerService.findAll();
        return ResponseEntity.ok(speakerMapper.toResponseDtoList(speakers));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar un speaker", description = "Actualiza los datos de un speaker existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Speaker actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Speaker no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<SpeakerResponseDto> updateSpeaker(@PathVariable Long id,
                                                            @Valid @RequestBody SpeakerRequestDto requestDto) {
        Speaker speaker = speakerService.update(id, requestDto);
        return ResponseEntity.ok(speakerMapper.toDto(speaker));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar un speaker", description = "Elimina un speaker existente identificado por su ID. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Speaker eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Speaker no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
