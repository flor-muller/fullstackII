package com.proyectointegrador.msplace.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Seat")
@Schema(name = "Detalles de Seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del asiento", example = "1")
    private Long id;

    @Schema(description = "Disponibilidad de asientos", example = "1")
    private Integer availability;

    @Schema(description = "Precio del asiento", example = "100.0")
    private Double price;

    @Column(name = "ticket_id")
    @Schema(description = "ID del Ticket", example = "1")
    private Long ticketId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    @Schema(description = "ID de la zona", example = "1")
    private Zone zone;
}