package com.proyectointegrador.msticket.repository.feign;

import com.proyectointegrador.msticket.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import com.proyectointegrador.msticket.configuration.feign.FeignInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Optional;

@FeignClient( name= "ms-users", url="http://3.87.66.156:8082", configuration = FeignInterceptor.class)
public interface FeignUserRepository {
    @RequestMapping(method = RequestMethod.GET, value = "/users/id/{id}")
    ResponseEntity<Optional<User>> findUserById(@PathVariable String id);
}
