package com.skinversity.backend.Models;

import com.skinversity.backend.Enumerators.Category;
import jakarta.persistence.*;
import jakarta.websocket.OnError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID productId;

    private String productSKU;

    private String productName;

    private BigDecimal productPrice;

    private String productDescription;

    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String productImageURL;

    private LocalDateTime productCreationDate;

    private LocalDateTime productUpdateDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Cart> cart;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Reviews> reviews;
}
