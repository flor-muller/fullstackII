package com.proyectointegrador.msplace.dto;

import com.proyectointegrador.msplace.domain.Zone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(name = "Detalles de SeatDTO")
public class SeatDTO {
    @Schema(description = "ID del asiento", example = "1")
    private Long id;

    @Schema(description = "Disponibilidad de asientos", example = "1")
    private Integer availability;

    @Schema(description = "Precio del asiento", example = "100.0")
    private Double price;

    @Schema(description = "ID de la zona", example = "1")
    private Zone zone;

    @Schema(description = "ID del Ticket", example = "1")
    private Long ticketId;
}
