package com.hfsolutions.rest.mapper;

import com.hfsolutions.rest.dto.product.response.ProductResponse;
import com.hfsolutions.rest.dto.product.response.RatingResponse;
import com.hfsolutions.rest.entity.Category;
import com.hfsolutions.rest.entity.Product;
import com.hfsolutions.rest.entity.Rating;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        Category category = product.getCategory();
        Rating rating = product.getRating();

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .categoryId(category != null ? category.getId() : null)
                .categoryName(category != null ? category.getName() : null)
                .image(product.getImage())
                .rating(rating != null ? RatingResponse.builder()
                        .rate(rating.getRate())
                        .count(rating.getCount())
                        .build() : null)
                .stock(product.getStock())
                .build();
    }
}
