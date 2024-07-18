package com.proyectointegrador.msevents.controller;

import com.proyectointegrador.msevents.domain.Category;
import com.proyectointegrador.msevents.dto.CategoryDTO;
import com.proyectointegrador.msevents.service.implement.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "Category Controller", description = "Operaciones relacionadas a las categorias")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Obtener categoria por Id", description = "Devuelve una categoria basado en Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @GetMapping("/public/getById/{id}")
    public ResponseEntity<?> getCategoryById(@Parameter(description = "ID de la categoria a obtener", example = "1") @PathVariable Long id) {
        ResponseEntity response = null;
        Optional<CategoryDTO> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            response = new ResponseEntity<>(category, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("Category with id of: " + id + " not found.", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Obtener categoria por Nombre", description = "Devuelve una categoria basado en el Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @GetMapping("/public/getByName/{name}")
    public ResponseEntity<?> getCategoryByName(@Parameter(description = "Nombre de la categoria a obtener", example = "Música") @PathVariable String name) {
        ResponseEntity response = null;
        Optional<CategoryDTO> category = categoryService.getCategoryByName(name);
        if (category.isPresent()) {
            response = new ResponseEntity<>(category, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("Category with name: " + name + " not found.", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Obtener todas las categorias", description = "Devuelve un Set de todas las categorias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias encontradas"),
            @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    @GetMapping("/public/get/all")
    public ResponseEntity<?> getAllCategories() {
        ResponseEntity response = null;
        Set<CategoryDTO> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            response = new ResponseEntity("No categories.", HttpStatus.NO_CONTENT);
        }
        else {
            response = new ResponseEntity<>(categories, HttpStatus.OK);
        }
        return response;
    }

    @Operation(summary = "Crear una categoria", description = "Crea una nueva categoria",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                        	"name": "Música"
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria creada"),
            @ApiResponse(responseCode = "500", description = "Error al crear la categoria")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO newCategoryDTO = categoryService.addCategory(categoryDTO);
            return new ResponseEntity<>("Category created successfully - " + newCategoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while creating a category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar una categoria", description = "Actualiza una categoria existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "id": 1,
                                        	"name": "Comedia"
                                        }
                                        """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria actualizada"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la categoria")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO newCategoryDTO = categoryService.updateCategory(categoryDTO);
            return new ResponseEntity<>("Category updated successfully - " + newCategoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating the category: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar una categoria", description = "Elimina la categoria basado en su Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la categoria")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteById/{id}")
    public ResponseEntity<?> deleteCategoryById(@Parameter(description = "Nombre de la categoria a eliminar", example = "Música") @PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return new ResponseEntity<>("Category with the id of: " + id + " successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting category with id of: " + id + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar una categoria", description = "Elimina la categoria basado en su Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la categoria")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteByName/{name}")
    public ResponseEntity<?> deleteCategoryByName(@Parameter(description = "ID de la categoria a eliminar", example = "1") @PathVariable String name) {
        try {
            categoryService.deleteCategoryByName(name);
            return new ResponseEntity<>("Category with the name: " + name + " successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting category with the name: " + name + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
