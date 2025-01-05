package com.skinversity.backend.Models;

import com.skinversity.backend.Enumerators.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customer_Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<Cart> carts;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<Cart> cartItems;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;
}
