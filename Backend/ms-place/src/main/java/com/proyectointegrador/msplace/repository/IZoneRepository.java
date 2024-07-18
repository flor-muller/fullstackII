package com.proyectointegrador.msplace.repository;

import com.proyectointegrador.msplace.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IZoneRepository extends JpaRepository<Zone, Long> {

    @Query("SELECT z FROM Zone z WHERE z.name = ?1 ORDER BY z.name")
    Optional<Zone> findByName(String name);
}
