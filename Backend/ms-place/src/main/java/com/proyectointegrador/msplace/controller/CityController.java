package com.proyectointegrador.msplace.controller;

import com.proyectointegrador.msplace.dto.CityDTO;
import com.proyectointegrador.msplace.dto.PlaceOnlyDTO;
import com.proyectointegrador.msplace.service.interfaces.ICityService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/city")
@Tag(name = "City Controller", description = "Operaciones relacionadas a la ciudad")
public class CityController {

    private final ICityService cityService;

    @Operation(summary = "Obtener ciudad por ID", description = "Devuelve una ciudad basado en Id")
    @GetMapping("/public/id/{id}")
    public Optional<CityDTO> getCityById(@Parameter(description = "Id de la ciudad a obtener", example = "1") @PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @Operation(summary = "Obtener ciudad por ID", description = "Devuelve una ciudad basado en el Nombre")
    @GetMapping("/public/name")
    public Optional<CityDTO> getCityByName(@Parameter(description = "Nombre de la ciudad a obtener", example = "Cordoba") @RequestParam("name") String name) {
        return cityService.getCityByName(name);
    }

    @Operation(summary = "Obtener todas las ciudades", description = "Devuelve un set de todas las ciudades")
    @GetMapping("/public/all")
    public Set<CityDTO> getAllCities() {
        return cityService.getAllCities();
    }

    @Operation(summary = "Obtener ciudad por ZipCode", description = "Devuelve una ciudad basado en el ZipCode(codigo postal)")
    @GetMapping("/public/zipCode/{zipCode}")
    public Set<CityDTO> getCityByZipCode(@Parameter(description = "ZipCode de la ciudad a obtener", example = "5000") @PathVariable String zipCode) {
        return cityService.getCityByZipCode(zipCode);
    }

    @Operation(summary = "Obtener todos los Places(estadios) por ID de la Ciudad", description = "Devuelve un Set de Places(estadios) por el Id de la ciudad")
    @GetMapping("/public/places/{id}")
    public Set<PlaceOnlyDTO> getAllPlacesByCityId(@Parameter(description = "ID de la ciudad para obtener los Places que hay", example = "1") @PathVariable Long id) {
        return cityService.getAllPlacesByCityId(id);
    }

    @Operation(summary = "Obtener todos los Places(estadios) por Nombre de la Ciudad", description = "Devuelve un Set de Places(estadios) por el Nombre de la ciudad")
    @GetMapping("/public/places/name")
    public Set<PlaceOnlyDTO> getAllPlacesByCityName(@Parameter(description = "Nombre de la ciudad para obtener los Places que hay", example = "Córdoba") @RequestParam("name") String name) {
        return cityService.getAllPlacesByCityName(name);
    }

    @Operation(summary = "Crear una ciudad", description = "Crea una nueva ciudad",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "name": "New York",
                                            "zipCode": "54321"
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ciudad creada"),
            @ApiResponse(responseCode = "500", description = "Error al crear la ciudad")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/add")
    public ResponseEntity<String> addCity(@RequestBody CityDTO cityDTO) {
        try {
            CityDTO cityDTOR = cityService.addCity(cityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ciudad creada con éxito: " + cityDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la ciudad: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar una ciudad", description = "Actualiza una ciudad existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": 1,
                                            "name": "New York",
                                            "zipCode": "12345"
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ciudad actualizada"),
            @ApiResponse(responseCode = "500", description = "Error al actualizar la ciudad")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<String> updateCity(@RequestBody CityDTO cityDTO) {
        try {
            CityDTO cityDTOR = cityService.updateCity(cityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ciudad actualizada con éxito: " + cityDTOR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la ciudad: " + e.getMessage());
        }
    }


    @Operation(summary = "Eliminar ciudad", description = "Elimina la ciudad basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la ciudad")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/{id}")
    public ResponseEntity<String> deleteCityById(@Parameter(description = "ID de la ciudad a eliminar", example = "1") @PathVariable Long id) {
        try {
            cityService.deleteCityById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Ciudad eliminada con éxito: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la ciudad: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar ciudad", description = "Elimina la ciudad basado en su Nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad eliminada"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar la ciudad")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/name")
    public ResponseEntity<String> deleteCityByName(@Parameter(description = "Nombre de la ciudad a eliminar", example = "Córdoba") @RequestParam("name") String name) {
        try {
            cityService.deleteCityByName(name);
            return ResponseEntity.status(HttpStatus.OK).body("Ciudad eliminada con éxito: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la ciudad: " + e.getMessage());
        }
    }
}