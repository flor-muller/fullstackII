package com.proyectointegrador.msticket.repository;

import com.proyectointegrador.msticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(String id);

    @Query("SELECT t FROM Ticket t WHERE t.eventId IN ?1")
    List<Ticket> findTicketsByEventIds(List<Long> eventIds);

    @Query("SELECT COUNT(DISTINCT t.eventId) FROM Ticket t WHERE t.eventId IN ?1")
    int countDistinctEventIds(List<Long> eventIds);

}
