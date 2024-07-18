package com.proyectointegrador.msplace.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msplace.domain.Event;
import com.proyectointegrador.msplace.domain.Seat;
import com.proyectointegrador.msplace.dto.SeatDTO;
import com.proyectointegrador.msplace.dto.SeatOnlyDTO;
import com.proyectointegrador.msplace.repository.EventRepository;
import com.proyectointegrador.msplace.repository.ISeatRepository;
import com.proyectointegrador.msplace.service.interfaces.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeatService implements ISeatService {

    private final ISeatRepository seatRepository;

    private final ZoneService zoneService;

    private final EventRepository eventRepository;

    private final ObjectMapper mapper;

    private SeatDTO saveSeat(SeatDTO seatDTO) {
        Seat seat = mapper.convertValue(seatDTO, Seat.class);
        seatRepository.save(seat);
        return mapper.convertValue(seat, SeatDTO.class);
    }

    @Override
    public Optional<SeatDTO> getSeatById(Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        SeatDTO seatDTO = null;
        if (seat.isPresent()) {
            seatDTO = mapper.convertValue(seat, SeatDTO.class);
            return Optional.ofNullable(seatDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<SeatDTO> getAllSeats() {
        List<Seat> seats = seatRepository.findAll();
        Set<SeatDTO> seatsDTO = new HashSet<>();
        for (Seat seat : seats) {
            seatsDTO.add(mapper.convertValue(seat, SeatDTO.class));
        }
        return seatsDTO;
    }

    @Override
    public SeatDTO addSeat(SeatDTO seatDTO) {
        return saveSeat(seatDTO);
    }

    @Override
    public SeatDTO updateSeat(SeatDTO seatDTO) {
        return saveSeat(seatDTO);
    }

    @Override
    public void deleteSeatById(Long id) {
        seatRepository.deleteById(id);
    }

    @Override
    public Integer getAvailability(Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            return seat.get().getAvailability();
        } else {
            return -1;
        }
    }

    @Override
    public SeatDTO putAvailability(Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            if (seat.get().getAvailability() == 1) {
                seat.get().setAvailability(0);
                seatRepository.save(seat.get());
                zoneService.putAvailability(0, seat.get().getZone().getId());
            } else if (seat.get().getAvailability() == 0) {
                seat.get().setAvailability(1);
                seatRepository.save(seat.get());
                zoneService.putAvailability(1, seat.get().getZone().getId());
            }
            SeatDTO seatDTO = mapper.convertValue(seat, SeatDTO.class);
            return seatDTO;
        } else {
            return null;
        }
    }

    @Override
    public Set<SeatOnlyDTO> getAllSeatsByZoneId(Long id) {
        List<Seat> seats = seatRepository.findAll();
        Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
        for (Seat seat : seats) {
            if (seat.getZone().getId().equals(id)) {
                seatsDTO.add(mapper.convertValue(seat, SeatOnlyDTO.class));
            }
        }
        return seatsDTO;
    }

    @Override
    public Set<SeatOnlyDTO> getSeatsByZoneName(String name) {
        List<Seat> seats = seatRepository.findAll();
        Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
        for (Seat seat : seats) {
            if (seat.getZone().getName().equals(name)) {
                seatsDTO.add(mapper.convertValue(seat, SeatOnlyDTO.class));
            }
        }
        return seatsDTO;
    }

    @Override
    public Set<SeatOnlyDTO> getSeatsAvailableByZoneId(Long id) {
        Set<SeatOnlyDTO> seatDTOs = getAllSeatsByZoneId(id);
        Set<SeatOnlyDTO> availableSeatDTOs = new HashSet<>();
        for (SeatOnlyDTO seatDTO : seatDTOs) {
            if (seatDTO.getAvailability() == 1) {
                availableSeatDTOs.add(seatDTO);
            }
        }
        return availableSeatDTOs;
    }

    @Override
    public Set<SeatOnlyDTO> getSeatsNotAvailableByZoneId(Long id) {
        Set<SeatOnlyDTO> seatDTOs = getAllSeatsByZoneId(id);
        Set<SeatOnlyDTO> availableSeatDTOs = new HashSet<>();
        for (SeatOnlyDTO seatDTO : seatDTOs) {
            if (seatDTO.getAvailability() == 0) {
                availableSeatDTOs.add(seatDTO);
            }
        }
        return availableSeatDTOs;
    }

    @Override
    public List<Seat> findByTicketId(Long id) {
        return seatRepository.findByTicketId(id);
    }

    @Override
    public Optional<Seat> updateSeatByTicket(Seat seat) {
        Optional<Seat> seatUpdated = seatRepository.findById(seat.getId());
        if (seatUpdated.isPresent()) {
            Seat existingSeat = seatUpdated.get();
            if (existingSeat.getAvailability() == null) {
                throw new IllegalStateException("Availability is null for seat with ID: " + existingSeat.getId());
            }
            existingSeat.setTicketId(seat.getTicketId());
            putAvailability(existingSeat.getId());
            return seatUpdated;
        } else {
            return Optional.empty();
        }
    }
}
