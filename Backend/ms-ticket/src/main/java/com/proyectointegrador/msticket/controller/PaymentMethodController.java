package com.proyectointegrador.msticket.controller;

import com.proyectointegrador.msticket.domain.PaymentMethod;
import com.proyectointegrador.msticket.service.interfaces.IPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paymentMethod")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final IPaymentMethodService paymentMethodService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/create")
    public ResponseEntity<PaymentMethod> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        PaymentMethod paymentMethodCreated = paymentMethodService.createPayment(paymentMethod);
        return ResponseEntity.ok(paymentMethodCreated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/private/update")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        PaymentMethod paymentMethodUpdated= paymentMethodService.updatePayment(paymentMethod);
        return ResponseEntity.ok(paymentMethodUpdated);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PaymentMethod> getPaymentById(@PathVariable Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodService.getPaymentById(id);
        return paymentMethod.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/public/all")
    public ResponseEntity<List<PaymentMethod>> getAllPayments() {
        List<PaymentMethod> paymentMethodList = paymentMethodService.getAllPayments();
        return ResponseEntity.ok(paymentMethodList);
    }

    @GetMapping("/private/ticket/{id}")
    public ResponseEntity<Optional<PaymentMethod>> getPaymentsByTicketId(@PathVariable Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodService.getPaymentsByTicketId(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @GetMapping("/public/category")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentsByCategory(@RequestParam("category") String category) {
        List<PaymentMethod> paymentMethod = paymentMethodService.getAllPaymentsByCategory(category);
        return ResponseEntity.ok(paymentMethod);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
