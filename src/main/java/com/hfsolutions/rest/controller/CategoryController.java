package com.hfsolutions.rest.controller;

import com.hfsolutions.rest.dto.category.request.CategoryCreateRequest;
import com.hfsolutions.rest.dto.category.response.CategoryResponse;
import com.hfsolutions.rest.dto.category.request.CategoryUpdateRequest;
import com.hfsolutions.rest.dto.common.ApiError;
import com.hfsolutions.rest.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Catálogo", description = "Gestión de categorías para la clasificación de productos")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Devuelve una lista completa de las categorías disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías recuperada exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))))
    })
    public List<CategoryResponse> list() {
        return categoryService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Recupera los detalles de una categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"RESOURCE_NOT_FOUND\", \"message\": \"Categoría no encontrada\", \"path\": \"/api/categories/987fcdeb-51a2-43d1-a5c6-987654321000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}")))
    })
    public CategoryResponse get(@Parameter(description = "ID de la categoría") @PathVariable UUID id) {
        return categoryService.get(id);
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría", description = "Registra una nueva categoría en el sistema. El nombre debe ser único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nombre inválido o duplicado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"BUSINESS_RULE_VIOLATION\", \"message\": \"La categoría 'Electrónica' ya existe\", \"path\": \"/api/categories\", \"timestamp\": \"2023-10-01T12:00:00Z\"}")))
    })
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest req) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(categoryService.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Modifica el nombre de una categoría existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"El nombre de la categoría no puede estar vacío\", \"path\": \"/api/categories/987fcdeb-51a2-43d1-a5c6-987654321000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Categoría no encontrada\", \"path\": \"/api/categories/987fcdeb-51a2-43d1-a5c6-987654321000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),

    })
    public CategoryResponse update(@Parameter(description = "ID de la categoría") @PathVariable UUID id,
                                   @Valid @RequestBody CategoryUpdateRequest req) {
        return categoryService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema. Solo es posible si no tiene productos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Categoría no encontrada\", \"path\": \"/api/categories/987fcdeb-51a2-43d1-a5c6-987654321000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar porque tiene productos asociados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"La categoría tiene productos asociados\", \"path\": \"/api/categories/987fcdeb-51a2-43d1-a5c6-987654321000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),

    })
    public ResponseEntity<Object> delete(@Parameter(description = "ID de la categoría") @PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.ok().body(java.util.Map.of("message", "Categoría eliminada exitosamente"));
    }
}