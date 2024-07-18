package com.proyectointegrador.msusers.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Data
@Schema(description = "Detalles de Ticket")
public class Ticket {
    @Schema(description = "ID del ticket", example = "1")
    private Long Id;

    @Schema(description = "ID del usuario al que le corresponde el ticket", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f")
    private String userId;
}
