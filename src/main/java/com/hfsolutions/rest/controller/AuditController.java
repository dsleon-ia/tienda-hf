package com.hfsolutions.rest.controller;

import com.hfsolutions.rest.audit.AuditAction;
import com.hfsolutions.rest.audit.ProductAudit;
import com.hfsolutions.rest.dto.common.ApiError;
import com.hfsolutions.rest.repository.mongo.ProductAuditRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Auditoría", description = "Consulta del historial de cambios y eventos del sistema (MongoDB)")
public class AuditController {
    private final ProductAuditRepository repository;

    public AuditController(ProductAuditRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Historial de un producto", description = "Obtiene la traza completa de cambios (creación, actualizaciones, stock) de un producto específico, ordenados cronológicamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial recuperado exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductAudit.class)))),

    })
    public List<ProductAudit> byProduct(@Parameter(description = "ID del producto") @PathVariable UUID productId) {
        return repository.findByProductIdOrderByTimestampDesc(productId);
    }

    @GetMapping("/products")
    @Operation(summary = "Últimos movimientos", description = "Recupera los 100 registros de auditoría más recientes de todo el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos recuperada exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductAudit.class)))),

    })
    public List<ProductAudit> latest() {
        return repository.findTop100ByOrderByTimestampDesc();
    }

    @GetMapping("/actions/{action}")
    @Operation(summary = "Filtrar por tipo de acción", description = "Busca eventos de auditoría según el tipo de operación realizada (CREATE, UPDATE, DELETE, etc.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filtrada recuperada exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductAudit.class)))),

    })
    public List<ProductAudit> byAction(@Parameter(description = "Tipo de acción a consultar") @PathVariable AuditAction action) {
        return repository.findByActionOrderByTimestampDesc(action);
    }
}