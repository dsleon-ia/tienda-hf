package com.hfsolutions.rest.repository.jpa;

import com.hfsolutions.rest.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Verifica si ya existe una categoría con un nombre específico, ignorando mayúsculas/minúsculas.
     * Cumple con la restricción implícita del requisito 3.1 de no tener nombres duplicados.
     * @param name Nombre de la categoría a verificar.
     * @return true si la categoría existe, false en caso contrario.
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca una categoría por su nombre, ignorando mayúsculas y minúsculas.
     * Se utiliza para evitar la creación de categorías con nombres duplicados.
     * @param name Nombre de la categoría a buscar.
     * @return Un Optional que contiene la categoría si se encuentra.
     */
    Optional<Category> findByNameIgnoreCase(String name);
}