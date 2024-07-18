package com.proyectointegrador.msplace.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msplace.domain.Seat;
import com.proyectointegrador.msplace.domain.Zone;
import com.proyectointegrador.msplace.dto.SeatOnlyDTO;
import com.proyectointegrador.msplace.dto.ZoneDTO;
import com.proyectointegrador.msplace.dto.ZoneOnlyDTO;
import com.proyectointegrador.msplace.repository.IZoneRepository;
import com.proyectointegrador.msplace.service.interfaces.IZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ZoneService implements IZoneService {

    private final IZoneRepository zoneRepository;

    // averiguar si se sigue usando el mapper o usar stream
    private final ObjectMapper mapper;

    private ZoneDTO saveZone(ZoneDTO zoneDTO) {
        Zone zone = mapper.convertValue(zoneDTO, Zone.class);
        zoneRepository.save(zone);
        return mapper.convertValue(zone, ZoneDTO.class);
    }

    private Set<SeatOnlyDTO> addInfoSeats(Set<Seat> seats) {
        Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
        for (Seat seat : seats) {
            seatsDTO.add(mapper.convertValue(seat, SeatOnlyDTO.class));
        }
        return seatsDTO;
    }

    @Override
    public Optional<ZoneDTO> getZoneById(Long id) {
        Optional<Zone> zone = zoneRepository.findById(id);
        if (zone.isPresent()) {
            ZoneDTO zoneDTO = mapper.convertValue(zone.get(), ZoneDTO.class);
            Set<SeatOnlyDTO> seatsDTO = addInfoSeats(zone.get().getSeats());
            zoneDTO.setSeats(seatsDTO);
            return Optional.of(zoneDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ZoneDTO> getZoneByName(String name) {
        Optional<Zone> zone = zoneRepository.findByName(name);
        if (zone.isPresent()) {
            ZoneDTO zoneDTO = mapper.convertValue(zone.get(), ZoneDTO.class);
            Set<SeatOnlyDTO> seatsDTO = addInfoSeats(zone.get().getSeats());
            zoneDTO.setSeats(seatsDTO);
            return Optional.of(zoneDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<ZoneDTO> getAllZones() {
        List<Zone> zones = zoneRepository.findAll();
        Set<ZoneDTO> zonesDTO = new HashSet<>();
        for (Zone zone : zones) {
            ZoneDTO zoneDTO = mapper.convertValue(zone, ZoneDTO.class);
            Set<SeatOnlyDTO> seatsDTO = addInfoSeats(zone.getSeats());
            zoneDTO.setSeats(seatsDTO);
            zonesDTO.add(zoneDTO);
        }
        return zonesDTO;
    }

    @Override
    public ZoneDTO addZone(ZoneDTO zoneDTO) {
        return saveZone(zoneDTO);
    }

    @Override
    public ZoneDTO updateZone(ZoneDTO zoneDTO) {
        return saveZone(zoneDTO);
    }

    @Override
    public void deleteZoneById(Long id) {
        zoneRepository.deleteById(id);
    }

    @Override
    public void deleteZoneByName(String name) {
        Optional<Zone> zone = zoneRepository.findByName(name);
        zone.ifPresent(zoneRepository::delete);
    }

    @Override
    public Integer getAvailability(Long id) {
        Optional<Zone> zone = zoneRepository.findById(id);
        if (zone.isPresent()) {
            return zone.get().getAvailability();
        } else {
            return -1;
        }
    }

    // ver si al modificar el del asiento, se modifica aca tambien
    @Override
    public void putAvailability(Integer number, Long id) {
        // si recibe un 0 es porque tiene que restar un asiento
        // si recibe un 1 es porque tiene que sumar un asiento
        if (number == 0) {
            Optional<Zone> zone = zoneRepository.findById(id);
            zone.ifPresent(value -> value.setAvailability(value.getAvailability() - 1));
        } else if (number == 1) {
            Optional<Zone> zone = zoneRepository.findById(id);
            zone.ifPresent(value -> value.setAvailability(value.getAvailability() + 1));
        }
    }

    @Override
    public Set<ZoneOnlyDTO> getAllZonesByPlaceId(Long id) {
        List<Zone> zones = zoneRepository.findAll();
        Set<ZoneOnlyDTO> zonesDTO = new HashSet<>();
        for (Zone zone : zones) {
            if (zone.getPlace().getId().equals(id)) {
                ZoneOnlyDTO zoneDTO = mapper.convertValue(zone, ZoneOnlyDTO.class);
                Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
                for (Seat seat : zone.getSeats()) {
                    SeatOnlyDTO seatDTO = mapper.convertValue(seat, SeatOnlyDTO.class);
                    seatsDTO.add(seatDTO);
                }
                zoneDTO.setSeats(seatsDTO);
                zonesDTO.add(zoneDTO);
            }
        }
        return zonesDTO;
    }
}
