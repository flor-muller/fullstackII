package com.proyectointegrador.msplace.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msplace.domain.City;
import com.proyectointegrador.msplace.domain.Place;
import com.proyectointegrador.msplace.domain.Seat;
import com.proyectointegrador.msplace.domain.Zone;
import com.proyectointegrador.msplace.dto.*;
import com.proyectointegrador.msplace.repository.ICityRepository;
import com.proyectointegrador.msplace.service.interfaces.ICityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CityService implements ICityService {

    private final ICityRepository cityRepository;

    private final PlaceService placeService;

    private final ObjectMapper mapper;

    private CityDTO saveCity(CityDTO cityDTO) {
        City city = mapper.convertValue(cityDTO, City.class);
        cityRepository.save(city);
        return mapper.convertValue(city, CityDTO.class);
    }

    private Set<PlaceOnlyDTO> addInfoPlace(Set<Place> places) {
        Set<PlaceOnlyDTO> placesDTO = new HashSet<>();
        for (Place place : places) {
            PlaceOnlyDTO placeDTO = mapper.convertValue(place, PlaceOnlyDTO.class);
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
            placeDTO.setZones(zonesDTO);
            placesDTO.add(placeDTO);
        }
        return placesDTO;
    }

    @Override
    public Optional<CityDTO> getCityById(Long id) {
        Optional<City> city = cityRepository.findById(id);
        CityDTO cityDTO = null;
        if (city.isPresent()) {
            cityDTO = mapper.convertValue(city, CityDTO.class);
            Set<PlaceOnlyDTO> placesDTO = addInfoPlace(city.get().getPlaces());
            cityDTO.setPlaces(placesDTO);
            return Optional.ofNullable(cityDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CityDTO> getCityByName(String name) {
        Optional<City> city = cityRepository.findByName(name);
        if (city.isPresent()) {
            CityDTO cityDTO = mapper.convertValue(city, CityDTO.class);
            Set<PlaceOnlyDTO> placesDTO = addInfoPlace(city.get().getPlaces());
            cityDTO.setPlaces(placesDTO);
            return Optional.ofNullable(cityDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Set<CityDTO> getAllCities() {
        List<City> cities = cityRepository.findAll();
        Set<CityDTO> citiesDTO = new HashSet<>();
        for (City city : cities) {
            CityDTO cityDTO = mapper.convertValue(city, CityDTO.class);
            Set<PlaceOnlyDTO> placesDTO = addInfoPlace(city.getPlaces());
            cityDTO.setPlaces(placesDTO);
            citiesDTO.add(cityDTO);
        }
        return citiesDTO;
    }

    @Override
    public Set<CityDTO> getCityByZipCode(String zipCode) {
        Set<City> cities = cityRepository.findByZipCode(zipCode);
        Set<CityDTO> citiesDTO = new HashSet<>();
        for (City city : cities) {
            CityDTO cityDTO = mapper.convertValue(city, CityDTO.class);
            Set<PlaceOnlyDTO> placesDTO = addInfoPlace(city.getPlaces());
            cityDTO.setPlaces(placesDTO);
            citiesDTO.add(cityDTO);
        }
        return citiesDTO;
    }

    @Override
    public CityDTO addCity(CityDTO cityDTO) {
        return saveCity(cityDTO);
    }

    @Override
    public CityDTO updateCity(CityDTO cityDTO) {
        return saveCity(cityDTO);
    }

    @Override
    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public void deleteCityByName(String name) {
        Optional<City> city = cityRepository.findByName(name);
        city.ifPresent(cityRepository::delete);
    }

    @Override
    public Set<PlaceOnlyDTO> getAllPlacesByCityId(Long id) {
        return placeService.getAllPlacesByCityId(id);
    }

    @Override
    public Set<PlaceOnlyDTO> getAllPlacesByCityName(String name) {
        Optional<CityDTO> city = getCityByName(name);
        if (city.isPresent()) {
            return placeService.getAllPlacesByCityId(city.get().getId());
        } else {
            return new HashSet<>();
        }
    }
}
