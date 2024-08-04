package com.proyectointegrador.msticket.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Ticket")
@Schema(description = "Detalles de Ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del ticket", example = "1")
    private Long Id;

    @Column(name ="user_id",nullable = false)
    @Schema(description = "ID del usuario", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f")
    private String userId;

    @Column(name ="event_id",nullable = false)
    private Long eventId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id", nullable = false)
    @Schema(description = "ID del método de pago", example = "1")
    private PaymentMethod paymentMethod;

    @Transient
    protected Event event;

    @Transient
    protected List<Seat> seats;
}
