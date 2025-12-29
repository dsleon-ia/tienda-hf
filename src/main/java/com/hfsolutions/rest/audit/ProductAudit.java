package com.hfsolutions.rest.audit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Document("product_audit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registro de auditoría de operaciones sobre productos")
public class ProductAudit {
    @Id
    @Schema(description = "Identificador único del registro de auditoría (MongoDB ID)", example = "64f1a2b3c4d5e6f7a8b9c0d1")
    private String id;

    @Schema(description = "ID del producto afectado", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;

    @Schema(description = "Tipo de acción realizada", example = "CREATE")
    private AuditAction action;

    @Schema(description = "Fecha y hora del evento", example = "2023-10-01T12:00:00Z")
    private Instant timestamp;

    @Schema(description = "Detalles del cambio (pares clave-valor)", example = "{\"price\": 99.99, \"stock\": 10}")
    private Map<String, Object> details;
}