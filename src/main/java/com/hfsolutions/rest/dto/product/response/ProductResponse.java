package com.hfsolutions.rest.dto.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación detallada de un producto")
public class ProductResponse {
    @Schema(description = "Identificador único del producto", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Título del producto", example = "Smartphone Samsung Galaxy S23")
    private String title;

    @Schema(description = "Descripción detallada", example = "Teléfono móvil con 256GB de almacenamiento, 8GB RAM.")
    private String description;

    @Schema(description = "Precio actual", example = "899.99")
    private BigDecimal price;

    @Schema(description = "Stock disponible", example = "50")
    private Integer stock;

    @Schema(description = "ID de la categoría asociada", example = "987fcdeb-51a2-43d1-a5c6-987654321000")
    private UUID categoryId;

    @Schema(description = "Nombre de la categoría asociada", example = "Electrónica")
    private String categoryName;

    @Schema(description = "Calificación del producto")
    private RatingResponse rating;

    @Schema(description = "URL de la imagen", example = "https://example.com/images/s23.jpg")
    private String image;
}
