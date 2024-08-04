package com.proyectointegrador.msticket.service.implement;

import com.proyectointegrador.msticket.domain.PaymentMethod;
import com.proyectointegrador.msticket.domain.Ticket;
import com.proyectointegrador.msticket.exception.PaymentMethodNotFoundException;
import com.proyectointegrador.msticket.repository.IPaymentMethodRepository;
import com.proyectointegrador.msticket.repository.ITicketRepository;
import com.proyectointegrador.msticket.service.interfaces.IPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    private final IPaymentMethodRepository paymentMethodRepository;
    private final ITicketRepository ticketRepository;

    @Override
    public Optional<PaymentMethod> getPaymentById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public List<PaymentMethod> getAllPayments() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod createPayment(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public PaymentMethod updatePayment(PaymentMethod paymentMethod) {
        if (!paymentMethodRepository.existsById(paymentMethod.getId())) {
            throw new PaymentMethodNotFoundException("Payment method not found");
        }
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void deletePayment(Long id) {
        if (!paymentMethodRepository.existsById(id)){
            throw new PaymentMethodNotFoundException("Payment method not found");
        }
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public Optional<PaymentMethod> getPaymentsByTicketId(Long ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.map(Ticket::getPaymentMethod);
    }

    @Override
    public List<PaymentMethod> getAllPaymentsByCategory(String category) {
        return paymentMethodRepository.findByCategory(category);
    }
}
