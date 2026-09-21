package com.stockdrive.repository;

import com.stockdrive.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByPartNumber(String partNumber);

    boolean existsByPartNumber(String partNumber);

    List<Product> findByProductNameContainingIgnoreCase(String productName);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByQuantityLessThanEqual(Integer minimumStock);
}