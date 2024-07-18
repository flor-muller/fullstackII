package com.proyectointegrador.msevents.dto;

import com.proyectointegrador.msevents.domain.Category;
import com.proyectointegrador.msevents.domain.DateEvent;
import com.proyectointegrador.msevents.domain.Images;
import com.proyectointegrador.msevents.domain.Place;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventGetDTO {
    private Long id;
    private String name;
    private String description;
    private Images images;
    private Category category;
    private DateEvent dateEvent;
    private Place place;
}
