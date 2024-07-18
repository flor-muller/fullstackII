package com.proyectointegrador.msplace.controller;

import com.proyectointegrador.msplace.domain.Event;
import com.proyectointegrador.msplace.domain.Place;
import com.proyectointegrador.msplace.dto.PlaceDTO;
import com.proyectointegrador.msplace.dto.PlaceOnlyDTO;
import com.proyectointegrador.msplace.dto.ZoneOnlyDTO;
import com.proyectointegrador.msplace.repository.EventRepository;
import com.proyectointegrador.msplace.service.implement.CityService;
import com.proyectointegrador.msplace.service.interfaces.ICityService;
import com.proyectointegrador.msplace.service.interfaces.IPlaceService;
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
@RequestMapping("/place")
@Tag(name = "Place Controller", description = "Operaciones relacionadas a Place")
public class PlaceController {

    private final IPlaceService placeService;

    private final ICityService cityService;

    @Operation(summary = "Obtener place(estadio) por ID", description = "Devuelve un place basado en Id")
    @GetMapping("/public/id/{id}")
    public Optional<PlaceDTO> getPlaceById(@Parameter(description = "Id del place a obtener", example = "1") @PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @Operation(summary = "Obtener place(estadio) por Nombre", description = "Devuelve un place basado en el Nombre")
    @GetMapping("/public/name")
    public Optional<PlaceDTO> getPlaceByName(@Parameter(description = "Id del place a obtener", example = "1") @RequestParam("name") String name) {
        return placeService.getPlaceByName(name);
    }

    @Operation(summary = "Obtener todos los places(estadios)", description = "Devuelve un set de todos los places")
    @GetMapping("/public/all")
    public Set<PlaceDTO> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @Operation(summary = "Obtener múltiples places por sus IDs", description = "Devuelve una lista de places basado en una lista de IDs")
    @GetMapping("/public/ids")
    public ResponseEntity<List<PlaceDTO>> getPlacesByIds(@RequestParam List<Long> ids) {
        List<PlaceDTO> places = placeService.getPlacesByIds(ids);
        return ResponseEntity.ok(places);
    }


    @Operation(summary = "Obtener todas las zonas por ID de place", description = "Devuelve un set de todas las zonas por ID de place")
    @GetMapping("/public/zones/{id}")
    public Set<ZoneOnlyDTO> getAllZonesByPlaceId(@Parameter(description = "Id del place para obtener las zonas", example = "1") @PathVariable Long id) {
        return placeService.getAllZonesByPlaceId(id);
    }

    @Operation(summary = "Obtener todas las zonas por el Nombre de place", description = "Devuelve un set de todas las zonas por el Nombre de place")
    @GetMapping("/public/zones/name")
    public Set<ZoneOnlyDTO> getAllZonesByPlaceName(@Parameter(description = "Nombre del place para obtener las zonas", example = "1") @RequestParam("name") String name) {
        return placeService.getAllZonesByPlaceName(name);
    }

    @GetMapping("/public/city/{city}")
    Set<PlaceOnlyDTO> getPlaceByCity(@PathVariable String city) {
        return cityService.getAllPlacesByCityName(city);
    }

    @Operation(summary = "Crear un place(estadio)", description = "Crea un nuevo place(estadio)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "name": "Arthur Ashe Stadium 3",
                                            "neighborhood": "Flushing Meadows",
                                            "street": "Arthur Ashe Stadium Rd",
                                            "number": 123,
                                            "city": {
                                                "id": 6
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estadio creado"),
            @ApiResponse(responseCode = "500", description = "Error al crear el estadio")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<String> addPlace(@RequestBody PlaceDTO placeDTO) {
        try {
            PlaceDTO placeDTOR = placeService.addPlace(placeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estadio registrado con éxito: " + placeDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el estadio: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un place(estadio)", description = "Actualiza un place(estadio) existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": 5,
                                            "name": "Arthur Ashe Stadium",
                                            "neighborhood": "Flushing Meadows",
                                            "street": "Arthur Ashe Stadium Rd",
                                            "number": 4567,
                                            "city": {
                                                "id": 5
                                            }
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estadio actualizado"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar el estadio")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<String> updatePlace(@RequestBody PlaceDTO placeDTO) {
        try {
            PlaceDTO placeDTOR = placeService.updatePlace(placeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estadio actualizado con éxito: " + placeDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estadio: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar place(estadio)", description = "Elimina el place(estadio) basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadio eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el estadio")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/{id}")
    public ResponseEntity<String> deletePlaceById(@Parameter(description = "ID del place a eliminar", example = "1") @PathVariable Long id) {
        try {
            placeService.deletePlaceById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Estadio eliminado con éxito: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el estadio: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar ciudad", description = "Elimina la ciudad basado en su Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la ciudad")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/name")
    public ResponseEntity<String> deletePlaceByName(@Parameter(description = "Nombre del place a eliminar", example = "Córdoba") @RequestParam("name") String name) {
        try {
            placeService.deletePlaceByName(name);
            return ResponseEntity.status(HttpStatus.OK).body("Estadio eliminado con éxito: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el estadio: " + e.getMessage());
        }
    }
}