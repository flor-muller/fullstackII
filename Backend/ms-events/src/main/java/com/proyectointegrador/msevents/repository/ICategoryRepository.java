package com.proyectointegrador.msevents.repository;

import com.proyectointegrador.msevents.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.id = ?1 ORDER BY c.name")
    Optional<Category> findCategoryById(Long id);

    @Query("SELECT c FROM Category c WHERE c.name = ?1")
    Optional<Category> findCategoryByName(String name);
}
