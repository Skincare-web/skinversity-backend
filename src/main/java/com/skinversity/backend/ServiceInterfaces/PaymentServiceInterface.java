package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Requests.PaymentResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentServiceInterface {
    PaymentResponse processPayment(String userEmail, int totalAmount);
    void refundPayment(UUID paymentId);
}
