package com.hfsolutions.rest.controller;

import com.hfsolutions.rest.dto.product.request.CreateProductRequest;
import com.hfsolutions.rest.dto.product.request.UpdateProductRequest;
import com.hfsolutions.rest.dto.product.request.UpdateStockRequest;


import com.hfsolutions.rest.dto.product.response.ProductResponse;
import com.hfsolutions.rest.dto.common.ApiError;
import com.hfsolutions.rest.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Inventario", description = "Operaciones para la gestión del ciclo de vida de los productos (creación, edición, consulta y eliminación)")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Registra un producto en el catálogo asociado a una categoría existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. precio negativo, título vacío)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"BUSINESS_RULE_VIOLATION\", \"message\": \"El precio debe ser mayor a 0\", \"path\": \"/api/products\", \"timestamp\": \"2023-10-01T12:00:00Z\"}")))
    })
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(productService.create(req));
    }

    @GetMapping
    @Operation(summary = "Listar productos paginados", description = "Obtiene una lista paginada de todos los productos activos (no eliminados).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos recuperada exitosamente")
    })
    public Page<ProductResponse> list(
            @Parameter(description = "Número de página (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Recupera los detalles de un producto específico mediante su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"RESOURCE_NOT_FOUND\", \"message\": \"Producto no encontrado\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}")))
    })
    public ProductResponse get(@Parameter(description = "ID único del producto") @PathVariable UUID id) {
        return productService.get(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar información de producto", description = "Modifica los datos generales de un producto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"El stock no puede ser negativo\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),
            @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Producto no encontrado\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),

    })
    public ProductResponse update(@Parameter(description = "ID del producto a actualizar") @PathVariable UUID id,
                                  @Valid @RequestBody UpdateProductRequest req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Realiza una eliminación lógica (soft delete) del producto, marcándolo como inactivo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Producto no encontrado\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),

    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID del producto a eliminar") @PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza únicamente la cantidad de stock disponible de un producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Valor de stock inválido (negativo)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"El stock no puede ser negativo\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000/stock\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Producto no encontrado\", \"path\": \"/api/products/123e4567-e89b-12d3-a456-426614174000/stock\", \"timestamp\": \"2023-10-01T12:00:00Z\"}"))),

    })
    public ProductResponse updateStock(@Parameter(description = "ID del producto") @PathVariable UUID id,
                                       @Valid @RequestBody UpdateStockRequest req) {
        return productService.updateStock(id, req);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Filtrar por categoría", description = "Obtiene una lista paginada de productos pertenecientes a una categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filtrada recuperada exitosamente"),

    })
    public Page<ProductResponse> byCategory(@Parameter(description = "ID de la categoría") @PathVariable UUID categoryId,
                                       @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
                                       @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.byCategory(categoryId, pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar por título", description = "Busca productos cuyo título contenga el texto proporcionado (búsqueda insensible a mayúsculas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados de búsqueda recuperados exitosamente"),

    })
    public Page<ProductResponse> search(@Parameter(description = "Texto a buscar") @RequestParam("q") String q,
                                   @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
                                   @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.search(q, pageable);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Filtrar por rango de precio", description = "Obtiene productos cuyo precio se encuentra dentro del rango especificado (inclusivo).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filtrada recuperada exitosamente"),

    })
    public ResponseEntity<Page<ProductResponse>> priceRange(@Parameter(description = "Precio mínimo") @RequestParam("min") BigDecimal min,
                                       @Parameter(description = "Precio máximo") @RequestParam("max") BigDecimal max,
                                       @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
                                       @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.priceRange(min, max, pageable));
    }
}