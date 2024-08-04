package com.proyectointegrador.msticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailDTO {
    private String receiver;
    private String subject;
    private String message;
}
