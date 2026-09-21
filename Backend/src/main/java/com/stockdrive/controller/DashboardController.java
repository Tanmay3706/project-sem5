package com.stockdrive.controller;

import com.stockdrive.model.DashboardResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public DashboardResponse getDashboardData() {

        Long totalProducts = ((Number) entityManager
                .createNativeQuery(
                        "SELECT COUNT(*) FROM products"
                )
                .getSingleResult())
                .longValue();

        Long totalSuppliers = ((Number) entityManager
                .createNativeQuery(
                        "SELECT COUNT(*) FROM suppliers"
                )
                .getSingleResult())
                .longValue();

        Long lowStockProducts = ((Number) entityManager
                .createNativeQuery(
                        "SELECT COUNT(*) FROM products " +
                        "WHERE quantity <= minimum_stock"
                )
                .getSingleResult())
                .longValue();

        Long pendingPurchaseOrders = ((Number) entityManager
                .createNativeQuery(
                        "SELECT COUNT(*) FROM purchase_orders " +
                        "WHERE status = 'PENDING'"
                )
                .getSingleResult())
                .longValue();

        return new DashboardResponse(
                totalProducts,
                totalSuppliers,
                lowStockProducts,
                pendingPurchaseOrders
        );
    }
}