package com.proyectointegrador.msticket.service.interfaces;

import com.proyectointegrador.msticket.domain.PaymentMethod;
import com.proyectointegrador.msticket.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface IPaymentMethodService {

    Optional<PaymentMethod> getPaymentById(Long id);
    List<PaymentMethod> getAllPayments();
    PaymentMethod createPayment(PaymentMethod paymentMethod);
    PaymentMethod updatePayment(PaymentMethod paymentMethod);
    void deletePayment(Long id);
    Optional<PaymentMethod> getPaymentsByTicketId(Long ticketId);
    List<PaymentMethod> getAllPaymentsByCategory(String category);

}
