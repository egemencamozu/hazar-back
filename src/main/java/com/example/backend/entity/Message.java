package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sender_id", nullable = false)
    private String senderId;
    
    @Column(name = "receiver_id", nullable = false)
    private String receiverId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "is_read")
    private Boolean isRead = false;
    
    @Column(name = "sender_name")
    private String senderName;
    
    @Column(name = "receiver_name")
    private String receiverName;
    
    @Column(name = "product_title")
    private String productTitle;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
