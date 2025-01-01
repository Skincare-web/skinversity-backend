package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.Models.Shipping;

import java.util.UUID;

public interface ShippingServiceInterface {
    double calculateShippingCost(UUID orderId);
    Shipping getDetails(UUID orderId);
}
