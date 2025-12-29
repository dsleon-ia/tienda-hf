package com.hfsolutions.rest.service;

import com.hfsolutions.rest.dto.category.request.CategoryCreateRequest;
import com.hfsolutions.rest.dto.category.request.CategoryUpdateRequest;
import com.hfsolutions.rest.dto.category.response.CategoryResponse;
import com.hfsolutions.rest.entity.Category;
import com.hfsolutions.rest.exception.BadRequestException;
import com.hfsolutions.rest.exception.NotFoundException;
import com.hfsolutions.rest.mapper.CategoryMapper;
import com.hfsolutions.rest.repository.jpa.CategoryRepository;
import com.hfsolutions.rest.repository.jpa.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream().map(CategoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    public CategoryResponse get(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        return CategoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse create(CategoryCreateRequest req) {
        if (categoryRepository.existsByNameIgnoreCase(req.getName())) throw new BadRequestException("La categoría ya existe");
        Category category = Category.builder().name(req.getName().trim()).build();
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse update(UUID id, CategoryUpdateRequest req) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        if (!category.getName().equalsIgnoreCase(req.getName()) && categoryRepository.existsByNameIgnoreCase(req.getName())) throw new BadRequestException("La categoría ya existe");
        category.setName(req.getName().trim());
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryResponse(category);
    }

    public void delete(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        if (productRepository.existsByCategory_IdAndDeletedFalse(id)) throw new BadRequestException("La categoría tiene productos asociados");
        categoryRepository.delete(category);
    }
}