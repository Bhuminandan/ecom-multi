package com.zinum.service;

import com.zinum.dto.request.CreateProductRequest;
import com.zinum.exception.ProductException;
import com.zinum.model.Product;
import com.zinum.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest request, Seller seller);
    public void deleteProduct(Long id) throws ProductException;
    public Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long id) throws ProductException;
    List<Product> searchProducts(String query);
    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );
    List<Product> getProductsBySellerId(Long sellerId);
}
