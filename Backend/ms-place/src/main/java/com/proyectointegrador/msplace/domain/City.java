package com.proyectointegrador.msplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString

@Schema(description = "Detalles de la Ciudad")
@Entity
@Table(name = "City")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la ciudad", example = "1")
    private Long id;

    @Schema(description = "Nombre de la ciudad", example = "Córdoba")
    private String name;

    @Column(name = "zip_code")
    @Schema(description = "Codigo Postal de la ciudad", example = "5000")
    private String zipCode;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(description = "Set de Places(estadios)")
    private Set<Place> places;
}
