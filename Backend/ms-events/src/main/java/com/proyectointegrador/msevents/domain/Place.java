package com.proyectointegrador.msevents.domain;

import lombok.*;

@AllArgsConstructor
@Data
public class Place {
    private Long id;
    private String name;
    private String neighborhood;
    private String street;
    private Integer number;
}
