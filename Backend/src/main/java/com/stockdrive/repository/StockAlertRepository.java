package com.stockdrive.repository;

import com.stockdrive.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StockAlertRepository extends JpaRepository<Product,Long> {
    @Query(value="SELECT * FROM products WHERE quantity <= minimum_stock",nativeQuery=true)
    List<Product> findLowStockProducts();
}