package com.proyectointegrador.msticket.repository;

import com.proyectointegrador.msticket.domain.Seat;
import com.proyectointegrador.msticket.repository.feign.FeignSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepository {

    private final FeignSeatRepository seatRepository;

    public List<Seat> findByTicketId(Long id) {
        return seatRepository.findByTicketId(id);
    }

    public Seat findSeatById(Long id) {
        return seatRepository.findSeatById(id);
    }

    public Optional<Seat> updateSeatByTicket(@RequestBody Seat seat) {
        return seatRepository.updateSeatByTicket(seat);
    }
}
