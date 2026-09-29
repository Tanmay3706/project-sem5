package com.stockdrive.repository;

import com.stockdrive.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    boolean existsByOrderNumber(String orderNumber);
}