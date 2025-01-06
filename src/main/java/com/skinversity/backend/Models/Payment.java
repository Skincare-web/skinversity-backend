package com.skinversity.backend.Models;

import com.skinversity.backend.Enumerators.PaymentMethod;
import com.skinversity.backend.Enumerators.PaymentStatus;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentID;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Order order;

    private BigDecimal amount;

    private PaymentStatus status;

    private LocalDateTime paymentDate;

    private PaymentMethod paymentMethod;
}
