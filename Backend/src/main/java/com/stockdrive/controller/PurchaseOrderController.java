package com.stockdrive.controller;

import com.stockdrive.model.PurchaseOrder;
import com.stockdrive.repository.PurchaseOrderRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins="*")
public class PurchaseOrderController {
    private final PurchaseOrderRepository repository;

    public PurchaseOrderController(PurchaseOrderRepository repository){
        this.repository=repository;
    }

    @GetMapping
    public List<PurchaseOrder> getAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PurchaseOrder order){
        if(order.getOrderNumber()==null || order.getOrderNumber().isBlank())
            return ResponseEntity.badRequest().body("Order number is required");

        if(order.getSupplierId()==null)
            return ResponseEntity.badRequest().body("Supplier ID is required");

        if(order.getOrderDate()==null)
            return ResponseEntity.badRequest().body("Order date is required");

        if(order.getTotalAmount()==null)
            order.setTotalAmount(BigDecimal.ZERO);

        if(order.getTotalAmount().compareTo(BigDecimal.ZERO)<0)
            return ResponseEntity.badRequest().body("Total amount cannot be negative");

        if(repository.existsByOrderNumber(order.getOrderNumber()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order number already exists");

        if(order.getStatus()==null)
            order.setStatus(PurchaseOrder.Status.PENDING);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PurchaseOrder data){
        PurchaseOrder order=repository.findById(id).orElse(null);
        if(order==null)
            return ResponseEntity.notFound().build();

        if(data.getOrderNumber()==null || data.getOrderNumber().isBlank())
            return ResponseEntity.badRequest().body("Order number is required");

        if(data.getSupplierId()==null || data.getOrderDate()==null)
            return ResponseEntity.badRequest().body("Supplier ID and order date are required");

        if(data.getTotalAmount()!=null && data.getTotalAmount().compareTo(BigDecimal.ZERO)<0)
            return ResponseEntity.badRequest().body("Total amount cannot be negative");

        if(!order.getOrderNumber().equals(data.getOrderNumber()) &&
                repository.existsByOrderNumber(data.getOrderNumber()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order number already exists");

        order.setOrderNumber(data.getOrderNumber());
        order.setSupplierId(data.getSupplierId());
        order.setOrderDate(data.getOrderDate());
        order.setStatus(data.getStatus()==null ? PurchaseOrder.Status.PENDING : data.getStatus());
        order.setTotalAmount(data.getTotalAmount()==null ? BigDecimal.ZERO : data.getTotalAmount());
        order.setNotes(data.getNotes());
        order.setCreatedBy(data.getCreatedBy());

        return ResponseEntity.ok(repository.save(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok("Purchase order deleted successfully");
    }
}