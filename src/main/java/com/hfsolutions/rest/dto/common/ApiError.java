package com.hfsolutions.rest.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Estructura estandarizada para reportar errores de la API")
public class ApiError {
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Descripción corta del error HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje detallado del error o lista de validaciones fallidas", example = "El precio debe ser mayor a 0")
    private String message;

    @Schema(description = "URI del recurso solicitado donde ocurrió el error", example = "/api/products")
    private String path;

    @Schema(description = "Marca de tiempo del error", example = "2023-10-01T12:00:00Z")
    private Instant timestamp;
}