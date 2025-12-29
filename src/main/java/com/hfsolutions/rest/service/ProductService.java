package com.hfsolutions.rest.service;

import com.hfsolutions.rest.audit.AuditAction;
import com.hfsolutions.rest.audit.ProductAuditEvent;
import com.hfsolutions.rest.dto.product.request.CreateProductRequest;
import com.hfsolutions.rest.dto.product.response.ProductResponse;
import com.hfsolutions.rest.dto.product.request.UpdateProductRequest;
import com.hfsolutions.rest.dto.product.request.UpdateStockRequest;
import com.hfsolutions.rest.entity.Category;
import com.hfsolutions.rest.entity.Product;
import com.hfsolutions.rest.entity.Rating;
import com.hfsolutions.rest.exception.NotFoundException;
import com.hfsolutions.rest.mapper.ProductMapper;
import com.hfsolutions.rest.repository.jpa.CategoryRepository;
import com.hfsolutions.rest.repository.jpa.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        Product product = Product.builder()
                .title(req.getTitle().trim())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .category(category)
                .deleted(false)
                .rating(new Rating(req.getRating(), 0))
                .image(req.getImage())
                .build();
        product = productRepository.save(product);
        publish(product.getId(), AuditAction.CREATE, map("title", product.getTitle()));
        return ProductMapper.toProductResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> list(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable).map(ProductMapper::toProductResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse get(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (product.isDeleted()) throw new NotFoundException("Producto no encontrado");
        return ProductMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest req) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
        
        boolean stockChanged = !product.getStock().equals(req.getStock());
        
        Map<String, Object> changes = new HashMap<>();
        if (!product.getTitle().equals(req.getTitle().trim())) changes.put("title", req.getTitle().trim());
        // Usar compareTo para BigDecimal para ignorar diferencias de escala (ej: 10.0 vs 10.00)
        if (product.getPrice().compareTo(req.getPrice()) != 0) changes.put("price", req.getPrice());
        if (!product.getDescription().equals(req.getDescription())) changes.put("description", req.getDescription());
        if (!product.getCategory().getId().equals(category.getId())) changes.put("category", category.getName());
        
        product.setTitle(req.getTitle().trim());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(category);
        
        Rating currentRating = product.getRating();
        if (currentRating == null) {
            currentRating = new Rating(req.getRating(), 0);
        } else {
            currentRating.setRate(req.getRating());
        }
        product.setRating(currentRating);
        
        product.setImage(req.getImage());
        product = productRepository.save(product);
        
        if (stockChanged) {
            publish(product.getId(), AuditAction.STOCK_UPDATE, map("stock", product.getStock()));
        }
        
        if (!changes.isEmpty()) {
            publish(product.getId(), AuditAction.UPDATE, changes);
        }
        
        return ProductMapper.toProductResponse(product);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (!product.isDeleted()) {
            product.setDeleted(true);
            productRepository.save(product);
            publish(product.getId(), AuditAction.DELETE, map("title", product.getTitle()));
        }
    }

    @Transactional
    public ProductResponse updateStock(UUID id, UpdateStockRequest req) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        product.setStock(req.getStock());
        product = productRepository.save(product);
        publish(product.getId(), AuditAction.STOCK_UPDATE, map("stock", product.getStock()));
        return ProductMapper.toProductResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> byCategory(UUID categoryId, Pageable pageable) {
        return productRepository.findByCategory_IdAndDeletedFalse(categoryId, pageable).map(ProductMapper::toProductResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String q, Pageable pageable) {
        return productRepository.findByDeletedFalseAndTitleContainingIgnoreCase(q, pageable).map(ProductMapper::toProductResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> priceRange(BigDecimal min, BigDecimal max, Pageable pageable) {
        return productRepository.findByDeletedFalseAndPriceBetween(min, max, pageable).map(ProductMapper::toProductResponse);
    }

    private void publish(UUID productId, AuditAction action, Map<String, Object> details) {
        eventPublisher.publishEvent(new ProductAuditEvent(productId, action, details));
    }

    private Map<String, Object> map(String k, Object v) {
        Map<String, Object> m = new HashMap<>();
        m.put(k, v);
        return m;
    }
}