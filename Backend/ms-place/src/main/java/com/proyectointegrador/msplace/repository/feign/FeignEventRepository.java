package com.proyectointegrador.msplace.repository.feign;

import com.proyectointegrador.msplace.configuration.feign.FeignInterceptor;
import com.proyectointegrador.msplace.domain.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient( name= "ms-events", url="http://localhost:8083", configuration = FeignInterceptor.class)
public interface FeignEventRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/event/public/findByPlaceId/{id}")
    ResponseEntity<List<Event>> findByPlaceId(@PathVariable Long id);
}