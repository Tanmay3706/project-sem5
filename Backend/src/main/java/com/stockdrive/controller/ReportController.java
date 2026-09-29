package com.stockdrive.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import jakarta.persistence.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins="*")
public class ReportController {
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/summary")
    public Map<String,Object> getSummary(){
        Map<String,Object> data=new LinkedHashMap<>();
        data.put("totalProducts",count("SELECT COUNT(*) FROM products"));
        data.put("totalSuppliers",count("SELECT COUNT(*) FROM suppliers"));
        data.put("lowStockProducts",count("SELECT COUNT(*) FROM products WHERE quantity <= minimum_stock"));
        data.put("totalPurchaseOrders",count("SELECT COUNT(*) FROM purchase_orders"));
        data.put("pendingPurchaseOrders",count("SELECT COUNT(*) FROM purchase_orders WHERE status='PENDING'"));
        data.put("completedPurchaseOrders",count("SELECT COUNT(*) FROM purchase_orders WHERE status='COMPLETED'"));
        data.put("inventoryValue",number("SELECT COALESCE(SUM(quantity*selling_price),0) FROM products"));
        data.put("purchaseOrderValue",number("SELECT COALESCE(SUM(total_amount),0) FROM purchase_orders"));
        return data;
    }

    @GetMapping("/products")
    public List<?> getProducts(){
        return entityManager.createNativeQuery(
            "SELECT id,product_name,category,brand,part_number,quantity,minimum_stock,selling_price FROM products ORDER BY product_name"
        ).getResultList();
    }

    @GetMapping("/purchase-orders")
    public List<?> getPurchaseOrders(){
        return entityManager.createNativeQuery(
            "SELECT id,order_number,supplier_id,order_date,status,total_amount FROM purchase_orders ORDER BY order_date DESC"
        ).getResultList();
    }

    private long count(String sql){
        return ((Number)entityManager.createNativeQuery(sql).getSingleResult()).longValue();
    }

    private double number(String sql){
        return ((Number)entityManager.createNativeQuery(sql).getSingleResult()).doubleValue();
    }
}