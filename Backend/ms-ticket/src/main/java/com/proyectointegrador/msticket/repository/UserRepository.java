package com.proyectointegrador.msticket.repository;

import com.proyectointegrador.msticket.domain.User;
import com.proyectointegrador.msticket.repository.feign.FeignUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final FeignUserRepository userRepository;

    public Optional<User> findUserById(String id) {
        ResponseEntity<Optional<User>> response = userRepository.findUserById(id);
        return response.getBody();
    }
}
