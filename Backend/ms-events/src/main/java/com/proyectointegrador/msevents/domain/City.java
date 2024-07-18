package com.proyectointegrador.msevents.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class City {
    private Long id;
    private String name;
    private String zipCode;
}
