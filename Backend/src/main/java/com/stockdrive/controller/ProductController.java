package com.stockdrive.controller;

import com.stockdrive.model.Product;
import com.stockdrive.repository.ProductRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(productRepository.findAll());
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(null)
                );
    }

    // SEARCH PRODUCT BY NAME
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String name) {

        return ResponseEntity.ok(
                productRepository
                        .findByProductNameContainingIgnoreCase(name)
        );
    }

    // ADD NEW PRODUCT
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {

        if (product.getProductName() == null ||
                product.getProductName().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Product name is required");
        }

        if (product.getCategory() == null ||
                product.getCategory().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Category is required");
        }

        if (product.getPartNumber() == null ||
                product.getPartNumber().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Part number is required");
        }

        if (productRepository.existsByPartNumber(
                product.getPartNumber())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Part number already exists");
        }

        if (product.getSellingPrice() == null ||
                product.getSellingPrice().signum() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Selling price cannot be negative");
        }

        if (product.getQuantity() == null ||
                product.getQuantity() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Quantity cannot be negative");
        }

        if (product.getMinimumStock() == null ||
                product.getMinimumStock() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Minimum stock cannot be negative");
        }

        Product savedProduct = productRepository.save(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody Product productDetails) {

        Product existingProduct = productRepository
                .findById(id)
                .orElse(null);

        if (existingProduct == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }

        if (productDetails.getProductName() == null ||
                productDetails.getProductName().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Product name is required");
        }

        if (productDetails.getCategory() == null ||
                productDetails.getCategory().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Category is required");
        }

        if (productDetails.getPartNumber() == null ||
                productDetails.getPartNumber().trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Part number is required");
        }

        if (productDetails.getSellingPrice() == null ||
                productDetails.getSellingPrice().signum() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Selling price cannot be negative");
        }

        if (productDetails.getQuantity() == null ||
                productDetails.getQuantity() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Quantity cannot be negative");
        }

        if (productDetails.getMinimumStock() == null ||
                productDetails.getMinimumStock() < 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Minimum stock cannot be negative");
        }

        // Check whether the new part number belongs to another product
        if (!existingProduct.getPartNumber()
                .equals(productDetails.getPartNumber())
                && productRepository.existsByPartNumber(
                        productDetails.getPartNumber())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Part number already exists");
        }

        existingProduct.setProductName(
                productDetails.getProductName());

        existingProduct.setCategory(
                productDetails.getCategory());

        existingProduct.setBrand(
                productDetails.getBrand());

        existingProduct.setPartNumber(
                productDetails.getPartNumber());

        existingProduct.setSellingPrice(
                productDetails.getSellingPrice());

        existingProduct.setQuantity(
                productDetails.getQuantity());

        existingProduct.setMinimumStock(
                productDetails.getMinimumStock());

        existingProduct.setSupplierId(
                productDetails.getSupplierId());

        Product updatedProduct =
                productRepository.save(existingProduct);

        return ResponseEntity.ok(updatedProduct);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {

        if (!productRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product not found");
        }

        productRepository.deleteById(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }
}