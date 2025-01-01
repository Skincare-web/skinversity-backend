package com.skinversity.backend.Repositories;

import com.skinversity.backend.Models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
