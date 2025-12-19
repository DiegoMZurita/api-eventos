package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.dto.CategoryDto;
import com.gestion.eventos.api.mapper.CategoryMapper;
import com.gestion.eventos.api.service.ICategoryService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Operaciones relacionadas con la gestión de categorias")
public class CategoryController {
    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar categorías", description = "Obtiene la lista completa de categorías disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de categorías obtenido exitosamente")
    })
    public ResponseEntity<List<CategoryDto>> getAllCategories(){

        List<Category> categories = categoryService.findAll();

        return ResponseEntity.ok(categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Obtener una categoría por ID", description = "Devuelve los detalles de una categoría específica según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id){
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría en el sistema. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        Category categoryToCreate = categoryMapper.toEntity(categoryDto);
        Category createdCategory = categoryService.save(categoryToCreate);

        return new ResponseEntity<>(categoryMapper.toDto(createdCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza una categoría existente identificada por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto){
        Category categoryToUpdate = categoryMapper.toEntity(categoryDto);
        Category updatedCategory = categoryService.update(id, categoryToUpdate);

        return ResponseEntity.ok(categoryMapper.toDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría existente identificada por su ID. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
