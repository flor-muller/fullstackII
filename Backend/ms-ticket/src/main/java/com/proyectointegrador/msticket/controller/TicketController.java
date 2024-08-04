package com.proyectointegrador.msticket.controller;


import com.proyectointegrador.msticket.domain.Ticket;
import com.proyectointegrador.msticket.dto.TicketAllDTO;
import com.proyectointegrador.msticket.dto.TicketCreateDTO;
import com.proyectointegrador.msticket.dto.TicketReportDTO;
import com.proyectointegrador.msticket.service.interfaces.ITicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @Operation(summary = "Crear un ticket", description = "Crea un nuevo ticket",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "userId": "f7049e0e-0a21-4e19-819d-bf8915f2998f",
                                            "paymentMethodId": 1,
                                            "seatsId": [1],
                                            "eventId": 1
                                        }
                                        """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket creado"),
    })
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketCreateDTO ticketRequest) throws MessagingException {
        Ticket ticketCreated = ticketService.createTicket(ticketRequest);
        return ResponseEntity.ok(ticketCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketAllDTO> getTicketsById(@PathVariable Long id) {
        Optional<TicketAllDTO> ticketAllDTO = ticketService.getTicketById(id);
        return ticketAllDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TicketAllDTO>> getAllTickets() {
        List<TicketAllDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/findByUserId/{id}")
    public ResponseEntity<List<Ticket>> findByUserId(@PathVariable String id) {
        return ResponseEntity.ok().body(ticketService.findByUserId(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> getTicketsByReportSearch(@RequestBody Map<String, String> criteria) {
        Map<String, Object> result = ticketService.getTicketsByReportSearch(criteria);
        if (((List<TicketReportDTO>) result.get("tickets")).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }


}