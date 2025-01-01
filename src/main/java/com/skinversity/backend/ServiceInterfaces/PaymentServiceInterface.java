package com.skinversity.backend.ServiceInterfaces;

import java.util.UUID;

public interface PaymentServiceInterface {
    void processPayment(UUID orderId, double amount);
    void refundPayment(UUID paymentId);
}
