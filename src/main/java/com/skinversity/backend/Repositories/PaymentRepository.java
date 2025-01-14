package com.skinversity.backend.Repositories;

import com.skinversity.backend.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional <Payment> findPaymentByOrder_OrderID(UUID orderOrderID);
}
