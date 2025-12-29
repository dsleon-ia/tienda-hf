package com.hfsolutions.rest.repository.jpa;

import com.hfsolutions.rest.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    /**
     * Busca todos los productos que no han sido eliminados lógicamente.
     * Cumple con el requisito 5.2: "GET /api/products".
     * @param pageable Objeto de paginación.
     * @return Página de productos.
     */
    Page<Product> findAllByDeletedFalse(Pageable pageable);

    /**
     * Verifica si existe al menos un producto activo en una categoría dada.
     * Útil para validaciones, por ejemplo, antes de eliminar una categoría.
     * @param categoryId ID de la categoría a verificar.
     * @return true si existen productos, false en caso contrario.
     */
    boolean existsByCategory_IdAndDeletedFalse(UUID categoryId);

    /**
     * Busca productos por ID de categoría, excluyendo los eliminados lógicamente.
     * Cumple con el requisito 5.2: "GET /api/products/category/{categoryId}".
     * @param categoryId ID de la categoría.
     * @param pageable Objeto de paginación.
     * @return Página de productos.
     */
    Page<Product> findByCategory_IdAndDeletedFalse(UUID categoryId, Pageable pageable);

    /**
     * Busca productos cuyo título contenga un texto, ignorando mayúsculas/minúsculas y excluyendo eliminados.
     * Cumple con el requisito 5.2: "GET /api/products/search?q=".
     * @param q Texto a buscar en el título.
     * @param pageable Objeto de paginación.
     * @return Página de productos.
     */
    Page<Product> findByDeletedFalseAndTitleContainingIgnoreCase(String q, Pageable pageable);

    /**
     * Busca productos dentro de un rango de precios, excluyendo los eliminados.
     * Cumple con el requisito 5.2: "GET /api/products/price-range?min=&max=".
     * @param min Precio mínimo.
     * @param max Precio máximo.
     * @param pageable Objeto de paginación.
     * @return Página de productos.
     */
    Page<Product> findByDeletedFalseAndPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    /**
     * Consulta para obtener los productos más caros, excluyendo los eliminados.
     * Aunque no es un requisito explícito, sirve como ejemplo de consulta compleja y es útil para analíticas.
     * @param limit Número de productos a devolver.
     * @return Lista de los productos más caros.
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false ORDER BY p.price DESC")
    List<Product> findTopNByPrice(Pageable limit);
}