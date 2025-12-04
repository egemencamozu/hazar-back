package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management operations")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create product", description = "Create a new product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Parameter(description = "Product details") @Valid @RequestBody ProductRequest request,
            @Parameter(description = "User ID of the product owner") @RequestHeader("X-User-Id") String userId) {
        ApiResponse<ProductResponse> response = productService.createProduct(request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all products", description = "Retrieve all available products")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        ApiResponse<List<ProductResponse>> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        ApiResponse<ProductResponse> response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    // ✅ YENİ: Kullanıcının kendi ürünleri (tümü - müsait olsun olmasın)
    @Operation(summary = "Get my products", description = "Retrieve all products owned by the current user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User products retrieved successfully")
    })
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getMyProducts(
            @Parameter(description = "User ID") @RequestHeader("X-User-Id") String userId) {
        ApiResponse<List<ProductResponse>> response = productService.getProductsByOwner(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get products by owner", description = "Retrieve all products owned by a specific user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Owner products retrieved successfully")
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByOwner(
            @Parameter(description = "Owner user ID") @PathVariable String ownerId) {
        ApiResponse<List<ProductResponse>> response = productService.getProductsByOwner(ownerId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search products", description = "Search for products using a query string")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @Parameter(description = "Search query") @RequestParam String query) {
        ApiResponse<List<ProductResponse>> response = productService.searchProducts(query);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to update this product")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "Updated product details") @Valid @RequestBody ProductRequest request,
            @Parameter(description = "User ID of the requester") @RequestHeader("X-User-Id") String userId) {
        ApiResponse<ProductResponse> response = productService.updateProduct(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete product", description = "Delete a product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to delete this product")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "User ID of the requester") @RequestHeader("X-User-Id") String userId) {
        ApiResponse<Void> response = productService.deleteProduct(id, userId);
        return ResponseEntity.ok(response);
    }
}
