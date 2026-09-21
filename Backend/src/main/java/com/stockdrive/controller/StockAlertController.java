package com.stockdrive.controller;

import com.stockdrive.model.Product;
import com.stockdrive.repository.StockAlertRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock-alerts")
@CrossOrigin(origins="*")
public class StockAlertController {
    private final StockAlertRepository repository;

    public StockAlertController(StockAlertRepository repository) {
        this.repository=repository;
    }

    @GetMapping
    public List<Product> getLowStockProducts() {
        return repository.findLowStockProducts();
    }
}