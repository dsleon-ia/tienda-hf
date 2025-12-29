package com.hfsolutions.rest.audit;

import com.hfsolutions.rest.repository.mongo.ProductAuditRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProductAuditListener {
    private final ProductAuditRepository repository;

    public ProductAuditListener(ProductAuditRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(ProductAuditEvent event) {
        try {
            ProductAudit audit = ProductAudit.builder()
                    .productId(event.productId())
                    .action(event.action())
                    .timestamp(Instant.now())
                    .details(event.details())
                    .build();
            repository.save(audit);
        } catch (Exception ignored) {
        }
    }
}