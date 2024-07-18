package com.proyectointegrador.msplace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Event {
    private Long id;
    private String name;
    private String description;
    private Long placeId;
}

