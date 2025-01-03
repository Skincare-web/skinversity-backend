package com.skinversity.backend.ServiceInterfaces;

import com.skinversity.backend.DTOs.ProductDTO;
import com.skinversity.backend.Enumerators.Category;
import com.skinversity.backend.Models.Product;
import com.skinversity.backend.Requests.AddProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductServiceInterface {
    List<ProductDTO> getAllProducts();
    Optional<ProductDTO> getProductById(UUID productId);
    Product addProduct(AddProductRequest request, MultipartFile image) throws IOException;
    void updateProduct(ProductDTO product);
    void deleteProduct(UUID productId);
    List<ProductDTO> getProductsByCategory(Category category);
}
