package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    private String category;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    // ✅ Long'dan String'e çevrildi
    @Column(name = "owner_id", nullable = false)
    private String ownerId;
    
    @Column(name = "owner_name")
    private String ownerName;
    
    @Column(name = "owner_email")
    private String ownerEmail;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
