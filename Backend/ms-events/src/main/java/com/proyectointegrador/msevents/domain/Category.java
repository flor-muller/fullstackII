package com.proyectointegrador.msevents.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Category")
@Schema(description = "Detalles de la Categoria")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la Categoria", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoria", example = "Música")
    private String name;
}
