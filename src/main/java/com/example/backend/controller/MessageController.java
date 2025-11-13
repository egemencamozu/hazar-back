package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @RequestHeader("X-User-Id") String senderId) {
        ApiResponse<MessageResponse> response = messageService.sendMessage(request, senderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getUserMessages(@PathVariable String userId) {
        ApiResponse<List<MessageResponse>> response = messageService.getUserMessages(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(
            @RequestParam Long productId,
            @RequestParam String userId1,
            @RequestParam String userId2) {
        ApiResponse<List<MessageResponse>> response = messageService.getConversation(productId, userId1, userId2);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/mark-read/{userId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable String userId) {
        ApiResponse<Void> response = messageService.markAsRead(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable String userId) {
        ApiResponse<Long> response = messageService.getUnreadCount(userId);
        return ResponseEntity.ok(response);
    }
}
