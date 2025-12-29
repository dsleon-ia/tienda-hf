package com.hfsolutions.rest.dto.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalles de la calificación del producto")
public class RatingResponse {
    @Schema(description = "Puntuación promedio (0-5)", example = "4.5")
    private Double rate;

    @Schema(description = "Cantidad total de votos", example = "120")
    private Integer count;
}