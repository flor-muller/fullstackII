package com.proyectointegrador.msevents.repository;

import com.proyectointegrador.msevents.domain.City;
import com.proyectointegrador.msevents.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query("SELECT e FROM Event e WHERE e.id = ?1 ORDER BY e.name")
    Optional<Event> findEventById(Long id);

    @Query("SELECT e FROM Event e WHERE e.name = ?1 ORDER BY e.name")
    Optional<Event> findEventByName(String name);

    @Query("SELECT e FROM Event e WHERE e.category.id = ?1")
    List<Event> findByCategory(Long id);

    @Query("SELECT e FROM Event e WHERE e.dateEvent.id = ?1")
    List<Event> findEventByDateEvent(Long id);


    @Query("SELECT e FROM Event e WHERE e.placeId = ?1")
    List<Event> findByPlaceId(Long id);
}
