package com.hfsolutions.rest.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Datos requeridos para crear un nuevo producto")
public class CreateProductRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    @Schema(description = "Título del producto", example = "Smartphone Samsung Galaxy S23", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Schema(description = "Descripción detallada del producto", example = "Teléfono móvil con 256GB de almacenamiento, 8GB RAM, color negro.")
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio unitario del producto. Debe ser mayor a 0.", example = "899.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad de stock disponible. No puede ser negativo.", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "ID único de la categoría a la que pertenece el producto", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    @Min(value = 0, message = "La calificación mínima es 0")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Schema(description = "Calificación inicial del producto (0-5)", example = "4.5")
    private Double rating;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/s23.jpg")
    private String image;
}