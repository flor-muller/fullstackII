package com.proyectointegrador.msevents.controller;

import com.proyectointegrador.msevents.dto.DateEventDTO;
import com.proyectointegrador.msevents.service.implement.DateEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dateEvent")
@Tag(name = "DateEvent Controller", description = "Operaciones relacionadas a los DateEvents")
public class DateEventController {

    private final DateEventService dateEventService;

    @Operation(summary = "Obtener DateEvent por Id", description = "Devuelve un DateEvent basado en Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DateEvent encontrado"),
            @ApiResponse(responseCode = "404", description = "DateEvent no encontrado")
    })
    @GetMapping("/public/getById/{id}")
    public ResponseEntity<?> getDateEventById(@Parameter(description = "ID del DateEvent a obtener", example = "1") @PathVariable Long id) {
        ResponseEntity response = null;
        Optional<DateEventDTO> eventDate = dateEventService.getDateEventById(id);
        if (eventDate.isPresent()) {
            response = new ResponseEntity<>(eventDate, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("Event date with id of: " + id + " not found", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Operation(summary = "Obtener DateEvent por Date", description = "Devuelve un DateEvent basado en Date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DateEvent encontrado"),
            @ApiResponse(responseCode = "404", description = "DateEvent no encontrado"),
            @ApiResponse(responseCode = "400", description = "Formato de fecha ingresado incorrecto")
    })
    @GetMapping("/public/getByDate/{date}")
    public ResponseEntity<?> getDateEventByDate(@Parameter(description = "Date del DateEvent a encontrar", example = "") @PathVariable String date) {
        ResponseEntity response = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date dateObj = dateFormat.parse(date);
            Optional<DateEventDTO> eventDate = dateEventService.getDateEventByDate(dateObj);
            if (eventDate.isPresent()) {
                response = new ResponseEntity<>(eventDate, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>("Event date with date of: " + dateObj + " not found", HttpStatus.NOT_FOUND);
            }
        } catch (ParseException e) {
            response = new ResponseEntity<>("Invalid date format: " + date, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Operation(summary = "Obtener todos los DateEvents", description = "Devuelve un Set de todos los DateEvents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DateEvents encontrados"),
            @ApiResponse(responseCode = "204", description = "Sin contenido")
    })
    @GetMapping("/public/get/all")
    public ResponseEntity<?> getAllDateEvents() {
        ResponseEntity response = null;
        Set<DateEventDTO> eventDates = dateEventService.getAllDateEvents();
        if (eventDates.isEmpty()) {
            response = new ResponseEntity<>("No event dates", HttpStatus.NO_CONTENT);
        }
        else {
            response = new ResponseEntity<>(eventDates, HttpStatus.OK);
        }
        return response;
    }

    @Operation(summary = "Crear un DateEvent", description = "Crea un nuevo DateEvent",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "date": "2024-05-26T23:47:00Z"
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "DateEvent creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear el DateEvent")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<?> addDateEvent(@RequestBody DateEventDTO dateEventDTO) {
        try {
            DateEventDTO newEventDate = dateEventService.addDateEvent(dateEventDTO);
            return new ResponseEntity<>("Event date created successfully - " + newEventDate, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while creating an event date: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar un DateEvent", description = "Actualiza un DateEvent existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "id": 1,
                                            "date": "2024-05-26T23:47:00Z"
                                        }
                                        """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "DateEvent actualizado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el DateEvent")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<?> updateDateEvent(@RequestBody DateEventDTO dateEventDTO) {
        try {
            DateEventDTO newEventDate = dateEventService.updateDateEvent(dateEventDTO);
            return new ResponseEntity<>("Event date updated successfully - " + newEventDate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating the event date: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar DateEvent", description = "Elimina el DateEvent basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DateEvent eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el DateEvent")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteById/{id}")
    public ResponseEntity<?> deleteDateEventById(@Parameter(description = "ID del DateEvent a eliminar", example = "1") @PathVariable Long id) {
        try {
            dateEventService.deleteDateEventById(id);
            return new ResponseEntity<>("Event date with id of: " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while deleting the event date with id of: " + id + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar DateEvent", description = "Elimina el DateEvent basado en Date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DateEvent eliminado"),
            @ApiResponse(responseCode = "404", description = "DateEvent no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el DateEvent")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/deleteByDate/{dateStr}")
    public ResponseEntity<?> deleteDateEventByDate(@Parameter(description = "Date del DateEvent a eliminar", example = "2024-05-26T23:47:00Z") @PathVariable String dateStr) {
        ResponseEntity response = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date dateObj = dateFormat.parse(dateStr);
            Optional<DateEventDTO> eventDate = dateEventService.getDateEventByDate(dateObj);
            if (eventDate.isPresent()) {
                dateEventService.deleteDateEventByDate(dateObj);
                response = new ResponseEntity<>(eventDate, HttpStatus.OK);
            } else {
                response = new ResponseEntity<>("Event date with date of: " + dateObj + " not found", HttpStatus.NOT_FOUND);
            }
        } catch (ParseException e) {
            response = new ResponseEntity<>("Invalid date format: " + dateStr, HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
