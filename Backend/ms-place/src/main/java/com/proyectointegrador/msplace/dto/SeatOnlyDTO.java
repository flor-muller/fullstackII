package com.proyectointegrador.msplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(name = "Detalles de SeatOnlyDTO")
public class SeatOnlyDTO {
    @Schema(description = "ID del asiento", example = "1")
    private Long id;

    @Schema(description = "Disponibilidad de asientos", example = "1")
    private Integer availability;

    @Schema(description = "Precio del asiento", example = "100.0")
    private Double price;

    @Schema(description = "ID del Ticket", example = "1")
    private Long ticketId;
}
