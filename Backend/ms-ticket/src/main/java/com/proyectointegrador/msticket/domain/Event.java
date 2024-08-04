package com.proyectointegrador.msticket.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Event {
    private Long id;
    private String name;
    private String description;
    private DateEvent dateEvent;
    private Place place;
    private Image images;
}
