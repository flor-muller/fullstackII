package com.proyectointegrador.msevents.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msevents.domain.DateEvent;
import com.proyectointegrador.msevents.dto.DateEventDTO;
import com.proyectointegrador.msevents.repository.IDateEventRepository;
import com.proyectointegrador.msevents.service.interfaces.IDateEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class DateEventService implements IDateEventService {

    private final IDateEventRepository dateEventRepository;

    private final ObjectMapper mapper;

    private DateEventDTO saveDateEvent(DateEventDTO dateEventDTO) {
        DateEvent dateEvent = mapper.convertValue(dateEventDTO, DateEvent.class);
        dateEventRepository.save(dateEvent);
        return mapper.convertValue(dateEvent, DateEventDTO.class);
    }

    @Override
    public Optional<DateEventDTO> getDateEventById(Long id) {
        Optional<DateEvent> eventDate = dateEventRepository.findDateEventById(id);
        DateEventDTO dateEventDTO = null;
        if (eventDate.isPresent()) {
            dateEventDTO = mapper.convertValue(eventDate, DateEventDTO.class);
            return Optional.ofNullable(dateEventDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<DateEventDTO> getDateEventByDate(Date date) {
        Optional<DateEvent> eventDate = dateEventRepository.findDateEventByDate(date);
        DateEventDTO dateEventDTO = null;
        if (eventDate.isPresent()) {
            dateEventDTO = mapper.convertValue(eventDate, DateEventDTO.class);
            return Optional.ofNullable(dateEventDTO);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Set<DateEventDTO> getAllDateEvents() {
        List<DateEvent> dateEvents = dateEventRepository.findAll();
        Set<DateEventDTO> eventDatesDTO = new HashSet<>();
        for (DateEvent dateEvent : dateEvents) {
            eventDatesDTO.add(mapper.convertValue(dateEvent, DateEventDTO.class));
        }
        return eventDatesDTO;
    }

    @Override
    public DateEventDTO addDateEvent(DateEventDTO dateEventDTO) {
        return saveDateEvent(dateEventDTO);
    }

    @Override
    public DateEventDTO updateDateEvent(DateEventDTO dateEventDTO) {
        return saveDateEvent(dateEventDTO);
    }

    @Override
    public void deleteDateEventById(Long id) {
        dateEventRepository.deleteById(id);
    }

    @Override
    public void deleteDateEventByDate(Date date) {
        Optional<DateEvent> eventDate = dateEventRepository.findDateEventByDate(date);
        eventDate.ifPresent(dateEventRepository::delete);
    }
}
