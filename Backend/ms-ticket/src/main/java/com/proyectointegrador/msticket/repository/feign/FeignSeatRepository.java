package com.proyectointegrador.msticket.repository.feign;

import com.proyectointegrador.msticket.configuration.feign.FeignInterceptor;
import com.proyectointegrador.msticket.domain.Seat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name= "ms-place", url="http://3.87.226.2:8084", configuration = FeignInterceptor.class)
public interface FeignSeatRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/seat/private/ticket/{id}")
    List<Seat> findByTicketId(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/seat/public/id/{id}")
    Seat findSeatById(@PathVariable Long id);

    @RequestMapping(method = RequestMethod.PUT, value = "/seat/private/updateSeatByTicket")
    Optional<Seat> updateSeatByTicket(@RequestBody Seat seat);
}
