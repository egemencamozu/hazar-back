package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private Boolean isAvailable;
    // ✅ Long'dan String'e çevrildi
    private String ownerId;
    private String ownerName;
    private String createdAt;
}