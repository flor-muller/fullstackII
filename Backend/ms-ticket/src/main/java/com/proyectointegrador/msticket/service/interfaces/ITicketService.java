package com.proyectointegrador.msticket.service.interfaces;

import com.proyectointegrador.msticket.domain.Ticket;
import com.proyectointegrador.msticket.dto.TicketAllDTO;
import com.proyectointegrador.msticket.dto.TicketCreateDTO;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ITicketService {
    Optional<TicketAllDTO> getTicketById(Long id);
    List<TicketAllDTO> getAllTickets();
    Ticket createTicket(TicketCreateDTO ticket) throws MessagingException;
    void deleteTicket(Long id);
    List<Ticket> findByUserId(String id);
    List<Ticket> findTicketsByEventIds(List<Long> eventIds);
    int countDistinctEventIds(List<Long> eventIds);
    Map<String, Object> getTicketsByReportSearch(Map<String, String> criteria);
}
