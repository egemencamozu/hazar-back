package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Message and conversation management operations")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Send message", description = "Send a new message")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message sent successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid message data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Parameter(description = "Message details") @Valid @RequestBody MessageRequest request,
            @Parameter(description = "Sender user ID") @RequestHeader("X-User-Id") String senderId) {
        ApiResponse<MessageResponse> response = messageService.sendMessage(request, senderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user messages", description = "Retrieve all messages for a specific user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Messages retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getUserMessages(
            @Parameter(description = "User ID") @PathVariable String userId) {
        ApiResponse<List<MessageResponse>> response = messageService.getUserMessages(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get conversation", description = "Retrieve conversation between two users for a specific product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conversation retrieved successfully")
    })
    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(
            @Parameter(description = "Product ID") @RequestParam Long productId,
            @Parameter(description = "First user ID") @RequestParam String userId1,
            @Parameter(description = "Second user ID") @RequestParam String userId2) {
        ApiResponse<List<MessageResponse>> response = messageService.getConversation(productId, userId1, userId2);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mark messages as read", description = "Mark all messages as read for a user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Messages marked as read successfully")
    })
    @PutMapping("/mark-read/{userId}")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @Parameter(description = "User ID") @PathVariable String userId) {
        ApiResponse<Void> response = messageService.markAsRead(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get unread count", description = "Get the number of unread messages for a user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unread count retrieved successfully")
    })
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @Parameter(description = "User ID") @PathVariable String userId) {
        ApiResponse<Long> response = messageService.getUnreadCount(userId);
        return ResponseEntity.ok(response);
    }
}
