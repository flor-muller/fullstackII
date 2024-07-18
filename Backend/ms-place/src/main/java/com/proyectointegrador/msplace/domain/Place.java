package com.proyectointegrador.msplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString

@Schema(name = "Detalles de Place(estadio)")
@Entity
@Table(name = "Place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id de Place", example = "1")
    private Long id;

    @Schema(description = "Nombre de Place", example = "Estadio Mario Alberto Kempes")
    private String name;

    @Schema(description = "Barrio de Place", example = "Parque Chateau Carreras")
    private String neighborhood;

    @Schema(description = "Calle de Place", example = "Av. Ramón Cárcano")
    private String street;

    @Schema(description = "Numero de la calle de Place", example = "1234")
    private Integer number;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    @Schema(description = "Ciudad donde se encuentra Place", example = "Córdoba")
    private City city;

    @OneToMany (mappedBy = "place", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(description = "Zonas que contiene Place", example = "A - B - C - D")
    private Set<Zone> zones;

    @Transient
    @Schema(description = "Lista de eventos que se albergan en Place")
    protected List<Event> events;
}
