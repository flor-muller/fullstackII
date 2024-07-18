package com.proyectointegrador.msusers.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@Schema(description = "Detalles de User")
public class User {
    @Schema(description = "ID del user", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f")
    private String id;

    @Schema(description = "Nombre de usuario", example = "ticketrekpi")
    private String userName;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Perez")
    private String lastName;

    @Schema(description = "Correo electronico del usuario", example = "ticketrekpi@gmail.com")
    private String email;

    @Schema(description = "Lista de tickets")
    protected List<Ticket> tickets;
}
