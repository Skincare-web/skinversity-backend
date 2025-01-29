package com.skinversity.backend.Repositories;


import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p from Product p where p.category = :category order by p.productName")
    List<Product> findByCategory(Category category);

    @Query("select p from Product p where p.productId = :productId")
    Optional<Product> findByProductId(UUID productId);
}
