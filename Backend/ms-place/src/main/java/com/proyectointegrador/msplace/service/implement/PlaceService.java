package com.proyectointegrador.msplace.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msplace.domain.Event;
import com.proyectointegrador.msplace.domain.Place;
import com.proyectointegrador.msplace.domain.Seat;
import com.proyectointegrador.msplace.domain.Zone;
import com.proyectointegrador.msplace.dto.*;
import com.proyectointegrador.msplace.repository.EventRepository;
import com.proyectointegrador.msplace.repository.IPlaceRepository;
import com.proyectointegrador.msplace.service.interfaces.IPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService implements IPlaceService {

    private final IPlaceRepository placeRepository;

    private final ZoneService zoneService;

    private final ObjectMapper mapper;

    private final EventRepository eventRepository;

    private PlaceDTO savePlace(PlaceDTO placeDTO) {
        Place place = mapper.convertValue(placeDTO, Place.class);
        placeRepository.save(place);
        return mapper.convertValue(place, PlaceDTO.class);
    }

    private Set<ZoneOnlyDTO> addInfoZones(Set<Zone> zones) {
        Set<ZoneOnlyDTO> zonesDTO = new HashSet<>();
        for (Zone zone : zones) {
            ZoneOnlyDTO zoneDTO = mapper.convertValue(zone, ZoneOnlyDTO.class);
            Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
            for (Seat seat : zone.getSeats()) {
                seatsDTO.add(mapper.convertValue(seat, SeatOnlyDTO.class));
            }
            zoneDTO.setSeats(seatsDTO);
            zonesDTO.add(zoneDTO);
        }
        return zonesDTO;
    }

    private void findEventsByPlace(Place place) {
        List<Event> events = eventRepository.findByPLaceId(place.getId());
        place.setEvents(events);
    }

    @Override
    public Optional<PlaceDTO> getPlaceById(Long id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            findEventsByPlace(place.get());
            PlaceDTO placeDTO = mapper.convertValue(place.get(), PlaceDTO.class);
            Set<ZoneOnlyDTO> zonesDTO = addInfoZones(place.get().getZones());
            placeDTO.setZones(zonesDTO);
            return Optional.ofNullable(placeDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PlaceDTO> getPlaceByName(String name) {
        Optional<Place> place = placeRepository.findByName(name);
        if (place.isPresent()) {
            findEventsByPlace(place.get());
            PlaceDTO placeDTO = mapper.convertValue(place.get(), PlaceDTO.class);
            Set<ZoneOnlyDTO> zonesDTO = addInfoZones(place.get().getZones());
            placeDTO.setZones(zonesDTO);
            return Optional.ofNullable(placeDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<PlaceDTO> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        Set<PlaceDTO> placesDTO = new HashSet<>();
        for (Place place : places) {
            findEventsByPlace(place);
            PlaceDTO placeDTO = mapper.convertValue(place, PlaceDTO.class);
            Set<ZoneOnlyDTO> zonesDTO = addInfoZones(place.getZones());
            placeDTO.setZones(zonesDTO);
            placesDTO.add(placeDTO);
        }
        return placesDTO;
    }

    @Override
    public List<PlaceDTO> getPlacesByIds(List<Long> ids) {
        List<Place> places = placeRepository.findAllById(ids);
        return places.stream()
                .map(place -> mapper.convertValue(place, PlaceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PlaceDTO addPlace(PlaceDTO placeDTO) {
        return savePlace(placeDTO);
    }

    @Override
    public PlaceDTO updatePlace(PlaceDTO placeDTO) {
        return savePlace(placeDTO);
    }

    @Override
    public void deletePlaceById(Long id) {
        placeRepository.deleteById(id);
    }

    @Override
    public void deletePlaceByName(String name) {
        Optional<Place> place = placeRepository.findByName(name);
        place.ifPresent(placeRepository::delete);
    }

    @Override
    public Set<ZoneOnlyDTO> getAllZonesByPlaceId(Long id) {
        return zoneService.getAllZonesByPlaceId(id);
    }

    @Override
    public Set<ZoneOnlyDTO> getAllZonesByPlaceName(String name) {
        Optional<PlaceDTO> place = getPlaceByName(name);
        if (place.isPresent()) {
            return zoneService.getAllZonesByPlaceId(place.get().getId());
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public Set<PlaceOnlyDTO> getAllPlacesByCityId(Long id) {
        List<Place> places = placeRepository.findAll();
        Set<PlaceOnlyDTO> placesDTO = new HashSet<>();
        for (Place place : places) {
            if (place.getCity().getId().equals(id)) {
                PlaceOnlyDTO placeDTO = mapper.convertValue(place, PlaceOnlyDTO.class);
                List<Event> events = eventRepository.findByPLaceId(place.getId());
                Set<ZoneOnlyDTO> zonesDTO = new HashSet<>();
                for (Zone zone : place.getZones()) {
                    ZoneOnlyDTO zoneDTO = mapper.convertValue(zone, ZoneOnlyDTO.class);
                    Set<SeatOnlyDTO> seatsDTO = new HashSet<>();
                    for (Seat seat : zone.getSeats()) {
                        seatsDTO.add(mapper.convertValue(seat, SeatOnlyDTO.class));
                    }
                    zoneDTO.setSeats(seatsDTO);
                    zonesDTO.add(zoneDTO);
                }
                placeDTO.setEvents(events);
                placeDTO.setZones(zonesDTO);
                placesDTO.add(placeDTO);
            }
        }
        return placesDTO;
    }
}
