package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request,
            @RequestHeader("X-User-Id") String userId) {
        ApiResponse<ProductResponse> response = productService.createProduct(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        ApiResponse<List<ProductResponse>> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ApiResponse<ProductResponse> response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    // ✅ YENİ: Kullanıcının kendi ürünleri (tümü - müsait olsun olmasın)
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getMyProducts(
            @RequestHeader("X-User-Id") String userId) {
        ApiResponse<List<ProductResponse>> response = productService.getProductsByOwner(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByOwner(@PathVariable String ownerId) {
        ApiResponse<List<ProductResponse>> response = productService.getProductsByOwner(ownerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestParam String query) {
        ApiResponse<List<ProductResponse>> response = productService.searchProducts(query);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            @RequestHeader("X-User-Id") String userId) {
        ApiResponse<ProductResponse> response = productService.updateProduct(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        ApiResponse<Void> response = productService.deleteProduct(id, userId);
        return ResponseEntity.ok(response);
    }
}
