package com.skinversity.backend.Models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shippingId;

    @OneToOne(mappedBy = "shipping", cascade = CascadeType.REMOVE)
    private Order order;

    private String carrier;

    private int quantity;

    private BigDecimal price;

}
