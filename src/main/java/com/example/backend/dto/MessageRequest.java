package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    // ✅ Long'dan String'e çevrildi
    @NotBlank(message = "Alıcı ID boş olamaz")
    private String receiverId;
    
    @NotNull(message = "Ürün ID boş olamaz")
    private Long productId;
    
    @NotBlank(message = "Mesaj içeriği boş olamaz")
    private String content;
}