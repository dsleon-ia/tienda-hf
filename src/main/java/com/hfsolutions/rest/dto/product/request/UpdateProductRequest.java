package com.hfsolutions.rest.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "Datos para actualizar un producto existente")
public class UpdateProductRequest {
    @NotBlank
    @Schema(description = "Título del producto", example = "Smartphone Samsung Galaxy S23 Ultra", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Descripción detallada del producto", example = "Versión Ultra con S-Pen incluido.")
    private String description;

    @NotNull
    @Positive
    @Schema(description = "Precio unitario del producto. Debe ser mayor a 0.", example = "1199.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @NotNull
    @Min(0)
    @Schema(description = "Cantidad de stock disponible. No puede ser negativo.", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @NotNull
    @Schema(description = "ID único de la categoría a la que pertenece el producto", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    @Schema(description = "Calificación del producto (0-5)", example = "4.8")
    private Double rating;

    @Schema(description = "URL de la imagen del producto", example = "https://example.com/images/s23-ultra.jpg")
    private String image;
}