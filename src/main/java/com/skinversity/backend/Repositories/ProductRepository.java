package com.skinversity.backend.Repositories;

import com.skinversity.backend.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
