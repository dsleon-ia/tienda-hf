package com.hfsolutions.rest.dto.category.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Representación de una categoría")
public class CategoryResponse {
    @Schema(description = "Identificador único de la categoría", example = "987fcdeb-51a2-43d1-a5c6-987654321000")
    private UUID id;

    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String name;
}
