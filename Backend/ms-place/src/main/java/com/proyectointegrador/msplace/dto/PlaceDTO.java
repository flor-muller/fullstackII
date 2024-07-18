package com.proyectointegrador.msplace.dto;

import com.proyectointegrador.msplace.domain.City;
import com.proyectointegrador.msplace.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(name = "Detalles de PlaceDTO")
public class PlaceDTO {
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

    @Schema(description = "Ciudad donde se encuentra Place", example = "Córdoba")
    private City city;

    @Schema(description = "Zonas que contiene Place", example = "A - B - C - D")
    private Set<ZoneOnlyDTO> zones;

    @Schema(description = "Lista de eventos que se albergan en Place")
    protected List<Event> events;
}
