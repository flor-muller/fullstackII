package com.proyectointegrador.msplace.repository;

import com.proyectointegrador.msplace.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ICityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE c.name = ?1 ORDER BY c.name")
    Optional<City> findByName(String name);

    @Query("SELECT c FROM City c WHERE c.zipCode = ?1 ORDER BY c.name")
    Set<City> findByZipCode(String zipCode);
}
