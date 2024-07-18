package com.proyectointegrador.msevents.service.interfaces;

import com.proyectointegrador.msevents.dto.DateEventDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface IDateEventService {

    Optional<DateEventDTO> getDateEventById(Long id);
    Optional<DateEventDTO> getDateEventByDate(Date date);

    Set<DateEventDTO> getAllDateEvents();

    DateEventDTO addDateEvent(DateEventDTO dateEventDTO);
    DateEventDTO updateDateEvent(DateEventDTO dateEventDTO);

    void deleteDateEventById(Long id);
    void deleteDateEventByDate(Date date);

}
