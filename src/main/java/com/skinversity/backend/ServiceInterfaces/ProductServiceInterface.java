package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductServiceInterface {
    List<ProductDTO> getAllProducts();
    Optional<ProductDTO> getProductById(UUID productId);
    ProductDTO addProduct(ProductDTO product);
    void updateProduct(ProductDTO product);
    void deleteProduct(UUID productId);
    List<ProductDTO> getProductsByCategory(Category category);
}
