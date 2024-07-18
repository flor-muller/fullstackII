package com.proyectointegrador.msplace.controller;

import com.proyectointegrador.msplace.dto.ZoneDTO;
import com.proyectointegrador.msplace.service.interfaces.IZoneService;
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
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/zone")
@Tag(name = "Zone Controller", description = "Operaciones relacionadas a Zone")
public class ZoneController {

    private final IZoneService zoneService;

    @Operation(summary = "Obtener zone(zona) por ID", description = "Devuelve un zone(zona) basado en Id")
    @GetMapping("/public/id/{id}")
    public Optional<ZoneDTO> getZoneById(@Parameter(description = "ID de la zone a obtener", example = "1") @PathVariable Long id) {
        return zoneService.getZoneById(id);
    }

    @Operation(summary = "Obtener zone(zona) por el Nombre", description = "Devuelve un zone(zona) basado en el Nombre")
    @GetMapping("/public/name")
    public Optional<ZoneDTO> getZoneByName(@Parameter(description = "Nombre de la zone a obtener", example = "Popular Norte") @RequestParam("name") String name) {
        return zoneService.getZoneByName(name);
    }

    @Operation(summary = "Obtener todas las zones(zonas)", description = "Devuelve un set de todas las zones(zonas)")
    @GetMapping("/public/all")
    public Set<ZoneDTO> getAllZones() {
        return zoneService.getAllZones();
    }

    @Operation(summary = "Obtener availability(disponibilidad) por ID de zone(zona)", description = "Devuelve la availability(disponibilidad) basado en el Id de zone(zona)")
    @GetMapping("/public/availability/{id}")
    public Integer getAvailability(@Parameter(description = "ID de la zone para obtener disponibilidad", example = "1") @PathVariable Long id) {
        return zoneService.getAvailability(id);
    }

    @Operation(summary = "Crear una zone(zona)", description = "Crear una nueva zone(zona)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "name": "Popular Norte",
                                            "quantitySeat": 150,
                                            "availability": 150,
                                            "place": {
                                                "id": 8
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zona creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear la zona")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<String> addZone(@RequestBody ZoneDTO zoneDTO) {
        try {
            ZoneDTO zoneDTOR = zoneService.addZone(zoneDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Zona registrada con éxito: " + zoneDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar la zona: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una zone(zona)", description = "Actualiza una zone(zona) existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": 15,
                                            "name": "Popular",
                                            "quantitySeat": 150,
                                            "availability": 1,
                                            "place": {
                                                "id": 5
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zona actualizada"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la zona")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<String> updateZone(@RequestBody ZoneDTO zoneDTO) {
        try {
            ZoneDTO zoneDTOR = zoneService.updateZone(zoneDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Zona actualizada con éxito: " + zoneDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la zona: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar zone(zona)", description = "Elimina la zone(zona) basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zona eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la zona")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/{id}")
    public ResponseEntity<String> deleteZoneById(@Parameter(description = "ID de la zona a eliminar") @PathVariable Long id) {
        try {
            zoneService.deleteZoneById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Zona eliminada con éxito: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la zona: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar zone(zona)", description = "Elimina la zone(zona) basado en su Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zona eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la zona")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/name")
    public ResponseEntity<String> deleteZoneByName(@Parameter(description = "Nombre de la zona a eliminar") @RequestParam("name") String name) {
        try {
            zoneService.deleteZoneByName(name);
            return ResponseEntity.status(HttpStatus.OK).body("Zona eliminada con éxito: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la zona: " + e.getMessage());
        }
    }
}