package com.proyectointegrador.msplace.repository;

import com.proyectointegrador.msplace.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p WHERE p.name = ?1 ORDER BY p.name")
    Optional<Place> findByName(String name);
}
