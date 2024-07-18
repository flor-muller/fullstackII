package com.proyectointegrador.msplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(name = "Detalles de CiudadDTO")
public class CityDTO {
    @Schema(description = "ID de la ciudad", example = "1")
    private Long id;

    @Schema(description = "Nombre de la ciudad", example = "Córdoba")
    private String name;

    @Schema(description = "Codigo Postal de la ciudad", example = "5000")
    private String zipCode;

    @Schema(description = "Set de Places(estadios)")
    private Set<PlaceOnlyDTO> places;
}
