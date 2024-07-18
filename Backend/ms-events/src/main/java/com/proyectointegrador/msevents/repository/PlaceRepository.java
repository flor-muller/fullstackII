package com.proyectointegrador.msevents.repository;

import com.proyectointegrador.msevents.domain.Place;
import com.proyectointegrador.msevents.repository.repository.FeignPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final FeignPlaceRepository placeRepository;

    public Optional<Place> getPlaceById(Long id){
        ResponseEntity<Optional<Place>> response = placeRepository.getPlaceById(id);
        return response.getBody();
    }

    public List<Place> getPlacesByIds(List<Long> ids) {
        ResponseEntity<List<Place>> response = placeRepository.getPlacesByIds(ids);
        return response.getBody();
    }

    public Set<Place> getPlaceByCity(String city) {
        ResponseEntity<Set<Place>> response = placeRepository.getPlaceByCity(city);
        return response.getBody();
    }

    public Optional<Place> getPlaceByName(String name){
        ResponseEntity<Optional<Place>> response = placeRepository.getPlaceByName(name);
        return response.getBody();
    }

}
