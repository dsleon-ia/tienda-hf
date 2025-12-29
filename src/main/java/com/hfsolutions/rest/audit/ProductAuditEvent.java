package com.hfsolutions.rest.audit;

import java.util.Map;
import java.util.UUID;

public record ProductAuditEvent(UUID productId, AuditAction action, Map<String, Object> details) { }