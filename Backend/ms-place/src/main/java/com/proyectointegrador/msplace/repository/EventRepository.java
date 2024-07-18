package com.proyectointegrador.msplace.repository;

import com.proyectointegrador.msplace.domain.Event;
import com.proyectointegrador.msplace.repository.feign.FeignEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepository {
    private final FeignEventRepository eventRepository;

    public List<Event> findByPLaceId(Long id) {
        ResponseEntity<List<Event>> response = eventRepository.findByPlaceId(id);
        return response.getBody();
    }
}
