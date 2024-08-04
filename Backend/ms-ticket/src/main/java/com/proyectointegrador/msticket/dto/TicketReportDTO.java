package com.proyectointegrador.msticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class TicketReportDTO {
    private String eventName;
    private String place;
    private Double ticketPrice;
    private Date eventDate;
}

