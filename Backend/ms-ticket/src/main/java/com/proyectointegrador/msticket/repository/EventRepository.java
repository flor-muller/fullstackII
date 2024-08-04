package com.proyectointegrador.msticket.repository;

import com.proyectointegrador.msticket.domain.Event;
import com.proyectointegrador.msticket.repository.feign.FeignEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EventRepository {

    private final FeignEventRepository eventRepository;

    public Event findEventById(Long id) {
        return eventRepository.findEventById(id);
    }

    public List<Long> getEventIdsByReportSearch(Map<String, String> criteria){
        return eventRepository.getEventIdsByReportSearch(criteria);
    }
}
