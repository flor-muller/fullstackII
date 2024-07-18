package com.proyectointegrador.msevents.repository.repository;

import com.proyectointegrador.msevents.configuration.feign.FeignInterceptor;
import com.proyectointegrador.msevents.domain.City;
import com.proyectointegrador.msevents.domain.Place;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(name= "ms-place", url="http://localhost:8084", configuration = FeignInterceptor.class)
public interface FeignPlaceRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/place/public/id/{id}")
    ResponseEntity<Optional<Place>> getPlaceById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/place/public/ids", params = "ids")
    ResponseEntity<List<Place>> getPlacesByIds(@RequestParam("ids") List<Long> ids);

    @RequestMapping(method = RequestMethod.GET, value = "/place/public/city/{city}", params = "ids")
    ResponseEntity<Set<Place>> getPlaceByCity(@PathVariable String city);

    @RequestMapping(method = RequestMethod.GET, value = "/place/public/name", params = "name")
    ResponseEntity<Optional<Place>> getPlaceByName(@RequestParam("name") String name);
}
