package com.proyectointegrador.msusers.repository.feign;

import com.proyectointegrador.msusers.configuration.feign.FeignInterceptor;
import com.proyectointegrador.msusers.domain.Ticket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

@FeignClient( name= "ms-ticket", url="http://localhost:8085", configuration = FeignInterceptor.class)
public interface FeignTicketRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/tickets/findByUserId/{id}")
    ResponseEntity<List<Ticket>> findByUserId(@PathVariable String id);
}
