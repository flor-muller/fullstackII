package com.proyectointegrador.msticket.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Seat {
    private Long id;
    private Double price;
    private Long ticketId;
}

