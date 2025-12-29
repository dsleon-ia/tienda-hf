package com.hfsolutions.rest.dto.product.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Solicitud para actualizar únicamente el stock de un producto")
public class UpdateStockRequest {
    @NotNull
    @Min(0)
    @Schema(description = "Nueva cantidad de stock disponible. No puede ser negativo.", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;
}