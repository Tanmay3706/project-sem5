package com.stockdrive.controller;

import com.stockdrive.model.Supplier;
import com.stockdrive.repository.SupplierRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins="*")
public class SupplierController {
    private final SupplierRepository repository;

    public SupplierController(SupplierRepository repository){
        this.repository=repository;
    }

    @GetMapping
    public List<Supplier> getAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Supplier supplier){
        if(supplier.getSupplierName()==null||supplier.getSupplierName().isBlank())
            return ResponseEntity.badRequest().body("Supplier name is required");

        if(supplier.getEmail()!=null&&!supplier.getEmail().isBlank()&&repository.existsByEmail(supplier.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(supplier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Supplier data){
        Supplier supplier=repository.findById(id).orElse(null);
        if(supplier==null)
            return ResponseEntity.notFound().build();

        if(data.getSupplierName()==null||data.getSupplierName().isBlank())
            return ResponseEntity.badRequest().body("Supplier name is required");

        if(data.getEmail()!=null&&!data.getEmail().isBlank()&&!data.getEmail().equalsIgnoreCase(supplier.getEmail())&&repository.existsByEmail(data.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");

        supplier.setSupplierName(data.getSupplierName());
        supplier.setContactPerson(data.getContactPerson());
        supplier.setPhone(data.getPhone());
        supplier.setEmail(data.getEmail());
        supplier.setAddress(data.getAddress());
        supplier.setCity(data.getCity());
        supplier.setState(data.getState());

        return ResponseEntity.ok(repository.save(supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!repository.existsById(id))
            return ResponseEntity.notFound().build();

        repository.deleteById(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}