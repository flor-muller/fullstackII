package com.proyectointegrador.msticket.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectointegrador.msticket.domain.*;
import com.proyectointegrador.msticket.dto.TicketAllDTO;
import com.proyectointegrador.msticket.dto.TicketCreateDTO;
import com.proyectointegrador.msticket.dto.EmailDTO;
import com.proyectointegrador.msticket.dto.TicketReportDTO;
import com.proyectointegrador.msticket.exception.PaymentMethodNotFoundException;
import com.proyectointegrador.msticket.exception.TicketNotFoundException;
import com.proyectointegrador.msticket.repository.*;
import com.proyectointegrador.msticket.service.interfaces.IEmailService;
import com.proyectointegrador.msticket.service.interfaces.ITicketService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final ITicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final IPaymentMethodRepository iPaymentMethodRepository;
    private final ObjectMapper mapper;
    private final IEmailService emailService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private void findSeatsByTicket(Ticket ticket) {
        List<Seat> seats = seatRepository.findByTicketId(ticket.getId());
        ticket.setSeats(seats);
    }

    @Override
    public Optional<TicketAllDTO> getTicketById(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Optional<TicketAllDTO> ticketAllDTO = Optional.ofNullable(mapper.convertValue(optionalTicket.get(), TicketAllDTO.class));
            findSeatsByTicket(optionalTicket.get());
            Optional<User> user = userRepository.findUserById(optionalTicket.get().getUserId());
            Event event = eventRepository.findEventById(optionalTicket.get().getEventId());
            if (user.isPresent()) {
                if (ticketAllDTO.isPresent()) {
                    ticketAllDTO.get().setUser(user.get());
                    ticketAllDTO.get().setEvent(event);
                    ticketAllDTO.get().setPaymentMethodId(optionalTicket.get().getPaymentMethod().getId());
                    List<Long> seatsId = new ArrayList<>();
                    for (Seat seat : optionalTicket.get().getSeats()) {
                        seatsId.add(seat.getId());
                    }
                    ticketAllDTO.get().setSeatsId(seatsId);
                    return ticketAllDTO;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<TicketAllDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        tickets.forEach(this::findSeatsByTicket);
        return tickets.stream()
                .map(ticket -> {
                    TicketAllDTO ticketDTO = mapper.convertValue(ticket, TicketAllDTO.class);
                    Optional<User> user = userRepository.findUserById(ticket.getUserId());
                    user.ifPresent(ticketDTO::setUser);
                    Event event = eventRepository.findEventById(ticket.getEventId());
                    ticketDTO.setSeatsId(ticket.getSeats().stream().map(Seat::getId).collect(Collectors.toList()));
                    ticketDTO.setPaymentMethodId(ticket.getPaymentMethod().getId());
                    ticketDTO.setEvent(event);
                    return ticketDTO;
                })
                .collect(Collectors.toList());
    }


    private String buildEmailMessage(User user, Ticket ticket, List<Seat> seats, Event event) {
        StringBuilder message = new StringBuilder();
        message.append("<h3><strong>¡Muchas gracias por tu compra, ").append(user.getFirstName()).append("!</strong></h3><br>")
                .append("<strong>Detalles de su compra:</strong><br><br>")
                .append("<strong>Número de compra:</strong> ").append(ticket.getId()).append("<br>")
                .append("<strong>Evento:</strong> ").append(event.getName()).append("<br>")
                .append("<strong>Fecha:</strong> ").append(event.getDateEvent().getDate()).append("<br>")
                .append("<strong>Lugar:</strong> ").append(event.getPlace().getName()).append("<br>")
                .append("<strong>Método de Pago:</strong> ").append(ticket.getPaymentMethod().getCategory()).append("<br>")
                .append("<strong>Tarjeta:</strong> ").append(ticket.getPaymentMethod().getDetail()).append("<br><br>")
                .append("<strong>Asientos:</strong><br>");
        for (Seat seat : seats) {
            message.append("- Número de asiento: ").append(seat.getId()).append("<br>");
        }
        message.append("<br><strong>¡Que lo disfrutes!</strong>");
        return message.toString();
    }

    @Override
    public Ticket createTicket(TicketCreateDTO ticketDTO) throws MessagingException {
        Ticket ticket = mapper.convertValue(ticketDTO, Ticket.class);
        PaymentMethod paymentMethod =
                iPaymentMethodRepository.findById(ticketDTO.getPaymentMethodId())
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found"));
        ticket.setPaymentMethod(paymentMethod);
        List<Seat> seats = new ArrayList<>();
        for (Long seatId : ticketDTO.getSeatsId()) {
            Seat seat = seatRepository.findSeatById(seatId);
            seat.setTicketId(ticket.getId());
            seats.add(seat);
        }
        ticket.setSeats(seats);
        Event event = eventRepository.findEventById(ticketDTO.getEventId());
        ticket.setEvent(event);
        ticket = ticketRepository.save(ticket);
        for (Seat seat : seats) {
            seat.setTicketId(ticket.getId());
            seatRepository.updateSeatByTicket(seat);
        }
        Optional<User> user = userRepository.findUserById(ticket.getUserId());
        if (user.isPresent()) {
            String receiver = user.get().getEmail();
            String subject = "Confirmación de compra";
            String message = buildEmailMessage(user.get(), ticket, seats, event);
            EmailDTO emailDTO = new EmailDTO(receiver, subject, message);
            emailService.sendMail(emailDTO);
        }
        return ticket;
    }

    @Override
    public void deleteTicket(Long id) {
        if(!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public List<Ticket> findByUserId(String id) {
        List<Ticket> ticketsUser = ticketRepository.findByUserId(id);
        for (Ticket ticket : ticketsUser) {
            Optional<Ticket> ticketSearch = ticketRepository.findById(ticket.getId());
            if (ticketSearch.isPresent()) {
                Ticket foundTicket = ticketSearch.get();
                List<Seat> seats = seatRepository.findByTicketId(ticket.getId());
                List<Seat> updatedSeats = new ArrayList<>();
                for (Seat seat : seats) {
                    if (seat.getTicketId().equals(ticket.getId())) {
                        Seat seatSearch = seatRepository.findSeatById(seat.getId());
                        updatedSeats.add(seatSearch);
                    }
                }
                ticket.setSeats(updatedSeats);
                Event event = eventRepository.findEventById(foundTicket.getEventId());
                ticket.setEvent(event);
                ticket.setPaymentMethod(foundTicket.getPaymentMethod());
            }
        }

        return ticketsUser;
    }

    @Override
    public List<Ticket> findTicketsByEventIds(List<Long> eventIds) {
        return ticketRepository.findTicketsByEventIds(eventIds);
    }

    @Override
    public int countDistinctEventIds(List<Long> eventIds) {
        return ticketRepository.countDistinctEventIds(eventIds);
    }

    @Override
    public Map<String, Object> getTicketsByReportSearch(Map<String, String> criteria) {
        List<Long> eventIds = eventRepository.getEventIdsByReportSearch(criteria);
        List<Ticket> tickets = new ArrayList<>();
        List<TicketReportDTO> ticketsReportDTO = new ArrayList<>();
        int distinctEventCount = 0;
        Double totalRevenue = 0.0;
        if (!eventIds.isEmpty()) {
            tickets = findTicketsByEventIds(eventIds);
            distinctEventCount = countDistinctEventIds(eventIds);
            if (!tickets.isEmpty()) {
                for (Ticket ticket : tickets) {
                    List<Seat> seats = seatRepository.findByTicketId(ticket.getId());
                    double ticketPrice = 0.0;
                    for (Seat seat : seats) {
                            ticketPrice += seat.getPrice();
                    }
                    Event event = eventRepository.findEventById(ticket.getEventId());
                    TicketReportDTO ticketReportDTO = new TicketReportDTO(
                            event.getName(),
                            event.getPlace().getName(),
                            ticketPrice,
                            event.getDateEvent().getDate()
                    );
                    ticketsReportDTO.add(ticketReportDTO);
                    totalRevenue += ticketPrice;
                }
            } else {
                ticketsReportDTO = Collections.emptyList();
                distinctEventCount = 0;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("tickets", ticketsReportDTO);
        result.put("totalTickets", tickets.size());
        result.put("totalDistinctEvents", distinctEventCount);
        result.put("totalRevenue", totalRevenue);
        return result;
    }
}
