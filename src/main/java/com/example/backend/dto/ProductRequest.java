package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Başlık boş olamaz")
    private String title;
    
    @NotBlank(message = "Açıklama boş olamaz")
    private String description;
    
    @NotBlank(message = "Resim URL'si boş olamaz")
    private String imageUrl;
    
    private String category;
    
    private Boolean isAvailable = true;
}