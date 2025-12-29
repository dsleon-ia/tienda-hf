package com.hfsolutions.rest.dto.product.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * DTO anidado para deserializar el objeto `rating` del fichero `resources/data.json`.
 * Modela la estructura de la calificación del producto para la carga de datos inicial.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RatingDataDTO {
    private double rate;
    private int count;
}
