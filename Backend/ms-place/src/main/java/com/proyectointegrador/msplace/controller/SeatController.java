package com.proyectointegrador.msplace.controller;

import com.proyectointegrador.msplace.domain.Seat;
import com.proyectointegrador.msplace.dto.SeatDTO;
import com.proyectointegrador.msplace.dto.SeatOnlyDTO;
import com.proyectointegrador.msplace.service.interfaces.ISeatService;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seat")
@Tag(name = "Seat Controller", description = "Operaciones relacionadas a Seat")
public class SeatController {

    private final ISeatService seatService;

    @Operation(summary = "Obtener seat(asiento) por ID", description = "Devuelve un seat(asiento) basado en Id")
    @GetMapping("/public/id/{id}")
    public Optional<SeatDTO> getSeatById(@Parameter(description = "ID del seat a obtener", example = "1") @PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @Operation(summary = "Obtener todos los seats(asientos)", description = "Devuelve un set de todos los places")
    @GetMapping("/public/all")
    public Set<SeatDTO> getAllSeats() {
        return seatService.getAllSeats();
    }

    @Operation(summary = "Obtener el availability(disponibilidad) de los seat por ID", description = "Devuelve la cantidad de seats(asientos) disponibles basado en ID")
    @GetMapping("/public/availability/{id}")
    public Integer getAvailability(@Parameter(description = "ID del seat a obtener para la disponibilidad", example = "1") @PathVariable Long id) {
        return seatService.getAvailability(id);
    }

    @Operation(summary = "Obtener todos los seats(asientos) por ID de la zona", description = "Devuelve un set de todos los seats(asientos) por ID de la zona")
    @GetMapping("/public/zone/id/{id}")
    public Set<SeatOnlyDTO> getSeatsByZoneId(@Parameter(description = "ID de la zona para obtener los asientos") @PathVariable Long id) {
        return seatService.getAllSeatsByZoneId(id);
    }

    @Operation(summary = "Obtener todos los seats(asientos) por el Nombre de la zona", description = "Devuelve un set de todos los seats(asientos) por Nombre de la zona")
    @GetMapping("/public/zone/name")
    public Set<SeatOnlyDTO> getSeatsByZoneName(@Parameter(description = "Nombre de la zona para obtener los asientos") @RequestParam("name") String name) {
        return seatService.getSeatsByZoneName(name);
    }

    @Operation(summary = "Obtener todos los seats(asientos) disponibles por ID de la zona", description = "Devuelve un set de todos los seats(asientos) disponibles por ID de la zona")
    @GetMapping("/public/zone/availability/{id}")
    public Set<SeatOnlyDTO> getSeatsAvailableByZoneId(@Parameter(description = "ID de la zona para obtener los asientos disponibles", example = "1") @PathVariable Long id) {
        return seatService.getSeatsAvailableByZoneId(id);
    }

    @Operation(summary = "Obtener todos los seats(asientos) no disponibles por ID de la zona", description = "Devuelve un set de todos los seats(asientos) no disponibles por ID de la zona")
    @GetMapping("/public/zone/noAvailability/{id}")
    public Set<SeatOnlyDTO> getSeatsNotAvailableByZoneId(@Parameter(description = "ID de la zona para obtener los asientos no disponibles", example = "1") @PathVariable Long id) {
        return seatService.getSeatsNotAvailableByZoneId(id);
    }

    @Operation(summary = "Obtener seat(asiento) por ID de ticket", description = "Devuelve una lista de seats(asientos) por el ID del ticket")
    @GetMapping("/private/ticket/{id}")
    public List<Seat> findByTicketId(@Parameter(description = "ID del ticket para obtener los asientos", example = "1") @PathVariable Long id) {
        return seatService.findByTicketId(id);
    }

    @PutMapping("/private/updateSeatByTicket")
    public Optional<Seat> updateSeatByTicket(@RequestBody Seat seat){
        return seatService.updateSeatByTicket(seat);
    }

    @Operation(summary = "Crear un seat(asiento)", description = "Crea un seat(asiento) nuevo",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "availability": 1,
                                            "price": 100.0,
                                            "zone": {
                                                "id": 19
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asiento creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear el asiento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<String> addSeat(@RequestBody SeatDTO seatDTO) {
        try {
            SeatDTO seatDTOR = seatService.addSeat(seatDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Asiento registrado con éxito: " + seatDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el asiento: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un seat(asiento)", description = "Actualiza un seat(asiento) existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/type",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": 16,
                                            "availability": 0,
                                            "price": 150.0,
                                            "zone": {
                                                "id": 19
                                            },
                                            "ticketId" : 8
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asiento actualizado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el asiento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<String> updateSeat(@RequestBody SeatDTO seatDTO) {
        try {
            SeatDTO seatDTOR = seatService.updateSeat(seatDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Asiento actualizado con éxito: " + seatDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el asiento: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar disponibilidad de asiento por ID", description = "Actualiza la disponibilidad del asiento por su ID")
    @PutMapping("/private/availability/{id}")
    public SeatDTO updateAvailability(@Parameter(description = "ID del asiento a actualizarle la disponibilidad", example = "1") @PathVariable Long id) {
        return seatService.putAvailability(id);
    }

    @Operation(summary = "Eliminar seat(asiento)", description = "Elimina el seat(asiento) basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asiento eliminado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el asiento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/{id}")
    public ResponseEntity<String> deleteSeat(@Parameter(description = "ID del asiento a eliminar") @PathVariable Long id) {
        try {
            seatService.deleteSeatById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Asiento eliminado con éxito: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el asiento: " + e.getMessage());
        }
    }
}