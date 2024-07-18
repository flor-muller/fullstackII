package com.proyectointegrador.msevents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectointegrador.msevents.domain.Event;
import com.proyectointegrador.msevents.dto.EventDTO;
import com.proyectointegrador.msevents.dto.EventGetDTO;
import com.proyectointegrador.msevents.service.implement.AwsService;
import com.proyectointegrador.msevents.service.implement.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
@Tag(name = "Event Controller", description = "Operaciones relacionadas a los eventos")
public class EventController {

    private final EventService eventService;

    private final AwsService awsService;

    @Operation(summary = "Obtener evento por Id", description = "Devuelve un evento basado en Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/public/getById/{id}")
    public ResponseEntity<?> getEventById(@Parameter(description = "ID del evento a obtener", example = "1") @PathVariable Long id) {
        ResponseEntity response = null;
        Optional<EventGetDTO> event = eventService.getEventById(id);
        if (event.isPresent()) {
            response = new ResponseEntity<>(event, HttpStatus.OK);
        }
        else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with the id of: " + id + " not found.");
        }
        return response;
    }

    @Operation(summary = "Obtener evento por Nombre", description = "Devuelve un evento basado en el Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/public/getByName/{name}")
    public ResponseEntity<?> getEventByName(@Parameter(description = "Nombre del evento", example = "After Hours til Dawn Tour") @PathVariable String name) {
        ResponseEntity response = null;
        Optional<EventGetDTO> event = eventService.getEventByName(name);
        if (event.isPresent()) {
            response = new ResponseEntity<>(event, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("Event with the name: " + name + " not found", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Obtener todos los eventos", description = "Devuelve un Set de todos los eventos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eventos encontrados"),
            @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    @GetMapping("/public/get/all")
    public ResponseEntity<?> getAllEvents() {
        ResponseEntity response = null;
        Set<EventGetDTO> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            response = new ResponseEntity<>("No events", HttpStatus.NO_CONTENT);
        }
        else {
            response = new ResponseEntity<>(events, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/public/search")
    public List<EventGetDTO> searchEvents(@RequestParam(required = false) String name, @RequestParam(required = false) String category, @RequestParam(required = false) String city, @RequestParam(required = false) String dateString) throws ParseException {
        Date date = null;
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            date = dateFormat.parse(dateString);
        }
        return eventService.searchEvents(name, category, city, date);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/idsByCriteria")
    public List<Long> getEventIdsByReportSearch(@RequestBody Map<String, String> criteria){
        if (criteria.isEmpty() || criteria.values().stream().allMatch(String::isEmpty)) {
            return new ArrayList<>();
        }
        List<Long> eventIds = eventService.searchEventsReport(criteria);
        return eventIds;
    }

    @Operation(summary = "Obtener evento por ID de estadio", description = "Devuelve una lista de eventos por ID de Place(Estadio)")
    @GetMapping("/public/findByPlaceId/{id}")
    public ResponseEntity<List<Event>> findByPlaceId(@Parameter(description = "ID del place a obtener", example = "1")@PathVariable Long id) {
        return ResponseEntity.ok().body(eventService.findByPlaceId(id));
    }

    @Operation(summary = "Crear un evento", description = "Crea un nuevo evento",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "name": "After Hours til Dawn World Tour",
                                                 "description": "The Weeknd's presents his two albums After Hours and Dawn FM in a world tour",
                                                 "photo": "theweeknd.jpg",
                                                 "placeId": 1,
                                                 "category": {
                                                     "id": 1
                                                 },
                                                 "dateEvent": {
                                                     "date": "2024-05-26T23:47:00Z"
                                                 }
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear el evento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<?> addEvent(@RequestParam(value="eventDTO") String eventDTOStr, @RequestPart(value = "file") MultipartFile file) throws Exception {
        if (file == null) {
            return new ResponseEntity<>("At least one photo is required", HttpStatus.BAD_REQUEST);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EventDTO eventDTO = objectMapper.readValue(eventDTOStr, EventDTO.class);
        try {
            awsService.uploadFile(file);
            StringBuilder response = new StringBuilder("The following files were successfully uploaded to the s3 bucket:\n");
            response.append(file.getOriginalFilename()).append("\n");
            EventDTO newEventDTO = eventService.addEvent(eventDTO, file);
            response.append("Event created successfully - ").append(newEventDTO);
            return new ResponseEntity<>(response.toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while creating an event: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar un evento", description = "Actualiza un evento existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": 1,
                                            "name": "Twelve Carat Toothache World Tour - Post Malone",
                                            "description": "Post Malone's world tour presenting his album Twelve Carat Toothache",
                                            "photo": "postmalone.jpg",
                                            "placeId": 1
                                            "category": {
                                                "id": 1,
                                                "name": "Música"
                                            },
                                            "dateEvent": {
                                                "id": 1,
                                                "date": "2024-05-26T23:47:00Z"
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento actualizado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el evento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<?> updateEvent(@RequestBody EventDTO eventDTO,@RequestParam(value = "id") Long id) {
        try {
            eventService.updateEvent(eventDTO);
            return ResponseEntity.ok("Event updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating event: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar evento", description = "Elimina el evento basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el evento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteById/{id}")
    public ResponseEntity<?> deleteEventById(@Parameter(description = "ID del evento a eliminar", example = "1") @PathVariable Long id) {
        try {
            eventService.deleteEventById(id);
            return new ResponseEntity<>("Event deleted with the id: " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting event with id: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar evento", description = "Elimina el evento basado en el Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el evento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteByName/{name}")
    public ResponseEntity<?> deleteEventByName(@Parameter(description = "Nombre del evento a eliminar", example = "After Hours til Dawn World Tour") @PathVariable String name) {
        try {
            eventService.deleteEventByName(name);
            return new ResponseEntity<>("Event deleted with the name: " + name + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting event with the name: " + name, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
