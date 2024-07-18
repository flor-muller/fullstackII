package com.proyectointegrador.msusers.controller;

import com.proyectointegrador.msusers.domain.User;
import com.proyectointegrador.msusers.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Operaciones relacionadas a Users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obtener informacion de usuario", description = "Devuelve la informacion del usuario: ID - User - FirstName - LastName - Email")
    @GetMapping("/info")
    public Map<String, String> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getClaimAsString("sub");
        String userName = jwt.getClaimAsString("preferred_username");
        String name = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String email = jwt.getClaimAsString("email");
        Map<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put("id", id);
        userInfo.put("userName", userName);
        userInfo.put("name", name);
        userInfo.put("lastName", lastName);
        userInfo.put("email", email);
        return userInfo;
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Obtener usuario por nombre de usuario", description = "Devuelve una lista de usuarios basado en el nombre de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<List<User>> findUserByUserName(@Parameter(description = "Nombre de usuario a encontrar", example = "ticketrekpi") @PathVariable String username){
        return ResponseEntity.ok(userService.findUserByUserName(username));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<User>> findUserById(@Parameter(description = "ID del usuario a obtener", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f") @PathVariable String id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario basado en el ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@Parameter(description = "ID del usuario a eliminar", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f") @PathVariable String id){
        userService.deleteUserById(id);
        String message = "User with ID '" + id + "' has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                            "id": "f7049e0e-0a21-4e19-819d-bf8915f2998f",
                                            "firstName": "Maria Paulina",
                                            "lastName": "Oberti Busso",
                                            "email": "paulinaobertibusso@gmail.com",
                                            "userName": "paulinaobertibusso"
                                        }
                                        """
                        )
                )
        )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @Operation(summary = "Obtener roles del usuario", description = "Devuelve los roles del usuario basado en el ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })

    @GetMapping("/roles/{id}")
    public ResponseEntity<List<String>> getUserRoles(@Parameter(description = "ID del usuario para obtener los roles", example = "f7049e0e-0a21-4e19-819d-bf8915f2998f") @PathVariable String id){
        return ResponseEntity.ok(userService.getUserRoles(id));
    }

    @Operation(summary = "Agregar roles al usuario", description = "Agrega roles al usuario mediante el ID y el Rol a asignar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/roles/addRole")
    public ResponseEntity<List<String>> addRoleToUser(@RequestParam("id") String id, @RequestParam("role") String role){
        return ResponseEntity.ok(userService.addRoleToUser(id, role));
    }

    @Operation(summary = "Eliminar roles al usuario", description = "Elimina roles al usuario mediante el ID y el Rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/roles/deleteRole")
    public ResponseEntity<String> deleteRoleToUser(@RequestParam("id") String id, @RequestParam("role") String role){
        userService.deleteRoleToUser(id, role);
        String message = "Role '" + role + "' has been successfully deleted from user with ID '" + id + "'.";
        return ResponseEntity.ok().body(message);
    }
}
