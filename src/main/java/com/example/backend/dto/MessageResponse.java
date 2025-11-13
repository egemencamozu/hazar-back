package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    // ✅ Long'dan String'e çevrildi
    private String senderId;
    private String senderName;
    // ✅ Long'dan String'e çevrildi
    private String receiverId;
    private String receiverName;
    private Long productId;
    private String productTitle;
    private String content;
    private Boolean isRead;
    private String createdAt;
}