package com.hfsolutions.rest.dto.category.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear una nueva categoría")
public class CategoryCreateRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de la categoría. Debe ser único.", example = "Electrónica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
