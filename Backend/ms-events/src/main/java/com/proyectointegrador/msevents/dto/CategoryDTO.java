package com.proyectointegrador.msevents.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "Detalles de CategoriaDTO")
public class CategoryDTO {
    @Schema(description = "ID de la Categoria", example = "1")
    private Long id;

    @Schema(description = "Nombre de la Categoria", example = "1")
    private String name;
}
