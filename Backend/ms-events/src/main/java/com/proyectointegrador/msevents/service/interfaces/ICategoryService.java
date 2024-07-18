package com.proyectointegrador.msevents.service.interfaces;

import com.proyectointegrador.msevents.dto.CategoryDTO;

import java.util.Optional;
import java.util.Set;

public interface ICategoryService {

    Optional<CategoryDTO> getCategoryById(Long id);
    Optional<CategoryDTO> getCategoryByName(String name);

    Set<CategoryDTO> getAllCategories();

    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);
}
