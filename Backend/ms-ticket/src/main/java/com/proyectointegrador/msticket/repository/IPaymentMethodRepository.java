package com.proyectointegrador.msticket.repository;

import com.proyectointegrador.msticket.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query("SELECT p FROM PaymentMethod p WHERE p.category = ?1")
    List<PaymentMethod> findByCategory(String category);
}
