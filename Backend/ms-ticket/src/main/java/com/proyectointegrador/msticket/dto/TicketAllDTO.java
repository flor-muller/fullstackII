package com.proyectointegrador.msticket.dto;

import com.proyectointegrador.msticket.domain.Event;
import com.proyectointegrador.msticket.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class TicketAllDTO {
    private User user;
    private Long paymentMethodId;
    private List<Long> seatsId;
    private Event event;
}
