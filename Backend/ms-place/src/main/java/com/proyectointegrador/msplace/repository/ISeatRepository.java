package com.proyectointegrador.msplace.repository;

import com.proyectointegrador.msplace.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ISeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTicketId(Long id);
}
