package com.hfsolutions.rest.dto.product.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO para la deserialización de productos desde el fichero `resources/data.json`.
 * Su propósito es servir como un objeto de transferencia de datos durante la carga inicial
 * de la base de datos, mapeando los campos del JSON a este objeto Java.
 * Se utiliza exclusivamente en la clase `DataInitializer`.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductDataDTO {
    private Long id;
    private String title;
    private double price;
    private String description;
    private String category;
    @JsonProperty("category_id")
    private Long categoryId;
    private String image;
    private RatingDataDTO rating;
}
