package com.proyectointegrador.msevents.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msevents.domain.Category;
import com.proyectointegrador.msevents.dto.CategoryDTO;
import com.proyectointegrador.msevents.repository.ICategoryRepository;
import com.proyectointegrador.msevents.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    private final ObjectMapper mapper;

    private CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = mapper.convertValue(categoryDTO, Category.class);
        categoryRepository.save(category);
        return mapper.convertValue(category, CategoryDTO.class);
    }

    @Override
    public Optional<CategoryDTO> getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findCategoryById(id);
        CategoryDTO categoryDTO = null;
        if (category.isPresent()) {
            categoryDTO = mapper.convertValue(category, CategoryDTO.class);
            return Optional.ofNullable(categoryDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryDTO> getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findCategoryByName(name);
        CategoryDTO categoryDTO = null;
        if (category.isPresent()) {
            categoryDTO = mapper.convertValue(category, CategoryDTO.class);
            return Optional.ofNullable(categoryDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Set<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        Set<CategoryDTO> categoriesDTO = new HashSet<>();
        for (Category category : categories) {
            categoriesDTO.add(mapper.convertValue(category, CategoryDTO.class));
        }
        return categoriesDTO;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        return saveCategory(categoryDTO);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        return saveCategory(categoryDTO);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findCategoryByName(name);
        category.ifPresent(categoryRepository::delete);
    }

}
