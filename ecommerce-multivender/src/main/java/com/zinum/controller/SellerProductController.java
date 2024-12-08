package com.zinum.controller;

import com.zinum.dto.request.CreateProductRequest;
import com.zinum.exception.ProductException;
import com.zinum.model.Product;
import com.zinum.model.Seller;
import com.zinum.service.Impl.ProductServiceImpl;
import com.zinum.service.Impl.SellerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers/products")
@Slf4j
public class SellerProductController {

    private final SellerServiceImpl sellerService;
    private final ProductServiceImpl productService;

    @Autowired
    public SellerProductController(SellerServiceImpl sellerService, ProductServiceImpl productService) {
        this.sellerService = sellerService;
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProductsBySellerId(
            @RequestHeader("Authorization") String token
    ) {
        log.info("Getting products for seller: {}", token);
        Seller seller = sellerService.getSellerProfile(token);
        List<Product> products = productService.getProductsBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateProductRequest request
    ) {
        Seller seller = sellerService.getSellerProfile(token);
        Product createdProduct = productService.createProduct(request, seller);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) throws ProductException {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product
    ) throws ProductException {
            Product updatedProduct = productService.updateProduct(productId, product);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

}
