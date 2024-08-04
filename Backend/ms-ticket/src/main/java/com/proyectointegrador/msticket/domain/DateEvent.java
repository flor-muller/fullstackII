package com.proyectointegrador.msticket.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@AllArgsConstructor
@Data
public class DateEvent {
    private Long id;
    private Date date;
}
