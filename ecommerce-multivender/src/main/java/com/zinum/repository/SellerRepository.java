package com.zinum.repository;

import com.zinum.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, String> {
    Seller findByEmail(String email);
}
