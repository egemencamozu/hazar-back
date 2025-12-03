package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.ProductService;
import com.example.backend.service.MessageService;
import com.example.backend.service.UserService;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Admin", description = "Admin operations for managing products, users and messages")
public class AdminController {
    private final ProductService productService;
    private final MessageService messageService;
    private final UserService userService;

    // Admin kontrolü
    private boolean isAdmin(String userId) {
        ApiResponse<UserResponse> userResponse = userService.getUserById(userId);
        if (userResponse.isSuccess() && userResponse.getData() != null) {
            return userResponse.getData().getIsAdmin();
        }
        return false;
    }

    // ✅ Tüm ürünleri listele (müsait olsun olmasın)
    @Operation(summary = "Get all products", description = "Retrieve all products including unavailable ones (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Admin access required")
    })
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @Parameter(description = "User ID for admin verification") @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<ProductResponse>> response = productService.getAllProductsIncludingUnavailable();
        return ResponseEntity.ok(response);
    }

    // ✅ İstediği ürünü sil
    @Operation(summary = "Delete product", description = "Delete a product by ID (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Admin access required"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID to delete") @PathVariable Long id,
            @Parameter(description = "User ID for admin verification") @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<Void> response = productService.adminDeleteProduct(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Tüm mesajları listele
    @Operation(summary = "Get all messages", description = "Retrieve all messages in the system (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Admin access required")
    })
    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getAllMessages(
            @Parameter(description = "User ID for admin verification") @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<MessageResponse>> response = messageService.getAllMessagesForAdmin();
        return ResponseEntity.ok(response);
    }

    // ✅ Belirli bir konuşmayı görüntüle
    @Operation(summary = "Get conversation", description = "View conversation between two users for a specific product (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conversation retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Admin access required")
    })
    @GetMapping("/messages/conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(
            @Parameter(description = "Product ID") @RequestParam Long productId,
            @Parameter(description = "First user ID") @RequestParam String userId1,
            @Parameter(description = "Second user ID") @RequestParam String userId2,
            @Parameter(description = "Admin user ID for verification") @RequestHeader("X-User-Id") String adminId) {
        
        if (!isAdmin(adminId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<MessageResponse>> response = messageService.getConversation(productId, userId1, userId2);
        return ResponseEntity.ok(response);
    }

    // ✅ Tüm kullanıcıları listele
    @Operation(summary = "Get all users", description = "Retrieve all users in the system (Admin only)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Admin access required")
    })
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @Parameter(description = "User ID for admin verification") @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<UserResponse>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
