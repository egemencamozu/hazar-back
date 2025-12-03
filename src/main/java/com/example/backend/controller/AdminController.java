package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.ProductService;
import com.example.backend.service.MessageService;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
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
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<ProductResponse>> response = productService.getAllProductsIncludingUnavailable();
        return ResponseEntity.ok(response);
    }

    // ✅ İstediği ürünü sil
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<Void> response = productService.adminDeleteProduct(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Tüm mesajları listele
    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getAllMessages(
            @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<MessageResponse>> response = messageService.getAllMessagesForAdmin();
        return ResponseEntity.ok(response);
    }

    // ✅ Belirli bir konuşmayı görüntüle
    @GetMapping("/messages/conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(
            @RequestParam Long productId,
            @RequestParam String userId1,
            @RequestParam String userId2,
            @RequestHeader("X-User-Id") String adminId) {
        
        if (!isAdmin(adminId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<MessageResponse>> response = messageService.getConversation(productId, userId1, userId2);
        return ResponseEntity.ok(response);
    }

    // ✅ Tüm kullanıcıları listele
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestHeader("X-User-Id") String userId) {
        
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403)
                .body(ApiResponse.error("Bu işlem için admin yetkisi gereklidir"));
        }

        ApiResponse<List<UserResponse>> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }
}
