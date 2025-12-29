package com.hfsolutions.rest.mapper;

import com.hfsolutions.rest.dto.category.response.CategoryResponse;
import com.hfsolutions.rest.entity.Category;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}