package com.zinum.repository;

import com.zinum.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findAllBySellerId(Long sellerId);

    @Query("""
    SELECT p FROM Product p\s
    LEFT JOIN p.category c\s
    WHERE :query IS NULL\s
    OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))\s
    OR (c IS NOT NULL AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')))
""")
    List<Product> searchProduct(@Param("query") String query);
}
