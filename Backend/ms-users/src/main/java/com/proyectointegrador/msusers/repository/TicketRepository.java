package com.proyectointegrador.msusers.repository;

import com.proyectointegrador.msusers.domain.Ticket;
import com.proyectointegrador.msusers.repository.feign.FeignTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final FeignTicketRepository ticketRepository;

    public List<Ticket> findByUserId(String id) {
        ResponseEntity<List<Ticket>> response = ticketRepository.findByUserId(id);
        return response.getBody();
    }
}
