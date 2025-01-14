package com.skinversity.backend.Repositories;

import com.skinversity.backend.Models.Order;
import com.skinversity.backend.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByUser_UserId(UUID userUserId);

    Optional <Order> findByReference(String reference);
}
