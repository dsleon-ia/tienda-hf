package com.hfsolutions.rest.dto.category.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Datos para actualizar una categoría existente")
public class CategoryUpdateRequest {
    @NotBlank
    @Schema(description = "Nuevo nombre de la categoría.", example = "Hogar y Jardín", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
