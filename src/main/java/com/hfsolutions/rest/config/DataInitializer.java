package com.hfsolutions.rest.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfsolutions.rest.entity.Category;
import com.hfsolutions.rest.entity.Product;
import com.hfsolutions.rest.entity.Rating;
import com.hfsolutions.rest.dto.product.data.ProductDataDTO;
import com.hfsolutions.rest.repository.jpa.CategoryRepository;
import com.hfsolutions.rest.repository.jpa.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile("!test")
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public DataInitializer(ProductRepository productRepository,
                           CategoryRepository categoryRepository,
                           ResourceLoader resourceLoader,
                           ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (productRepository.count() == 0) {
            // Load data from JSON
            Resource resource = resourceLoader.getResource("classpath:data.json");
            InputStream inputStream = resource.getInputStream();
            List<ProductDataDTO> productDTOs = objectMapper.readValue(inputStream, new TypeReference<List<ProductDataDTO>>() {});

            Map<String, Category> categoryCache = new HashMap<>();
            List<Product> productsToSave = new ArrayList<>();

            for (ProductDataDTO dto : productDTOs) {
                System.out.println("Procesando producto: " + dto.getTitle() + " | Rating: " + dto.getRating());
                Category category = categoryCache.computeIfAbsent(dto.getCategory(), name -> {
                    Category newCategory = Category.builder()
                            .name(name)
                            .build();
                    return categoryRepository.save(newCategory);
                });

                Product product = Product.builder()
                                .title(dto.getTitle())
                                .price(BigDecimal.valueOf(dto.getPrice()))
                                .description(dto.getDescription())
                                .image(dto.getImage())
                                .category(category)
                                .deleted(false)
                                .stock(100)
                                .rating(dto.getRating() != null ?
                                        Rating.builder()
                                                .rate(dto.getRating().getRate())
                                                .count(dto.getRating().getCount())
                                                .build()
                                        : null)
                                .build();

                productsToSave.add(product);
            }

            productRepository.saveAll(productsToSave);
        }
    }
}
