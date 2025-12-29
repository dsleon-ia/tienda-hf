package com.hfsolutions.rest.repository.mongo;

import com.hfsolutions.rest.audit.AuditAction;
import com.hfsolutions.rest.audit.ProductAudit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ProductAuditRepository extends MongoRepository<ProductAudit, String> {

    /**
     * Busca todo el historial de auditoría para un producto específico.
     * Cumple con el requisito 6: "GET /api/audit/products/{productId}".
     * Se ordena por 'timestamp' descendente para mostrar los eventos más recientes primero.
     * @param productId ID del producto a consultar.
     * @return Lista de registros de auditoría.
     */
    List<ProductAudit> findByProductIdOrderByTimestampDesc(UUID productId);

    /**
     * Obtiene los últimos 100 registros de auditoría de la base de datos.
     * Cumple con el requisito 6: "GET /api/audit/products".
     * La ordenación descendente por 'timestamp' asegura que se obtienen los más recientes.
     * @return Lista de los 100 registros de auditoría más recientes.
     */
    List<ProductAudit> findTop100ByOrderByTimestampDesc();

    /**
     * Filtra los registros de auditoría por un tipo de acción específico.
     * Cumple con el requisito 6: "GET /api/audit/actions/{action}".
     * @param action Tipo de acción a filtrar (CREATE, UPDATE, etc.).
     * @return Lista de registros de auditoría que coinciden con la acción.
     */
    List<ProductAudit> findByActionOrderByTimestampDesc(AuditAction action);
}