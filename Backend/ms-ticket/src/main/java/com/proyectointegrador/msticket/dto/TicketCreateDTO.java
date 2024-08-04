package com.proyectointegrador.msticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Detalles de TicketCreateDTO")
public class TicketCreateDTO {
    @Schema(description = "ID del usuario", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f")
    private String userId;
    @Schema(description = "ID del método de pago", example = "1")
    private Long paymentMethodId;
    @Schema(description = "Lista con ID de los asientos")
    private List<Long> seatsId;
    private Long eventId;
}
