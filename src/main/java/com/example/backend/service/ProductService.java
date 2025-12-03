package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ApiResponse<ProductResponse> createProduct(ProductRequest request, String userId) {
        User owner = userRepository.findById(userId)
            .orElse(null);

        if (owner == null) {
            return ApiResponse.error("Kullanıcı bulunamadı");
        }

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setIsAvailable(true);
        // ✅ Long.parseLong kaldırıldı - direkt String
        product.setOwnerId(userId);
        product.setOwnerName(owner.getName());
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        
        return ApiResponse.success("Ürün başarıyla oluşturuldu", convertToResponse(savedProduct));
    }

    public ApiResponse<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findByIsAvailableTrue();
        List<ProductResponse> responses = products.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    public ApiResponse<ProductResponse> getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElse(null);

        if (product == null) {
            return ApiResponse.error("Ürün bulunamadı");
        }

        return ApiResponse.success(convertToResponse(product));
    }

    public ApiResponse<List<ProductResponse>> getProductsByOwner(String ownerId) {
        // ✅ Long.parseLong kaldırıldı
        List<Product> products = productRepository.findByOwnerId(ownerId);
        List<ProductResponse> responses = products.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    public ApiResponse<List<ProductResponse>> searchProducts(String query) {
        List<Product> products = productRepository
            .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        List<ProductResponse> responses = products.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    @Transactional
    public ApiResponse<ProductResponse> updateProduct(Long id, ProductRequest request, String userId) {
        Product product = productRepository.findById(id)
            .orElse(null);

        if (product == null) {
            return ApiResponse.error("Ürün bulunamadı");
        }

        if (!product.getOwnerId().equals(userId)) {
            return ApiResponse.error("Bu ürünü düzenleme yetkiniz yok");
        }

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        
        // ✅ isAvailable null kontrolü
        if (request.getIsAvailable() != null) {
            product.setIsAvailable(request.getIsAvailable());
        }

        Product updatedProduct = productRepository.save(product);
        
        return ApiResponse.success("Ürün güncellendi", convertToResponse(updatedProduct));
    }

    @Transactional
    public ApiResponse<Void> deleteProduct(Long id, String userId) {
        Product product = productRepository.findById(id)
            .orElse(null);

        if (product == null) {
            return ApiResponse.error("Ürün bulunamadı");
        }

        // ✅ Long.parseLong kaldırıldı - direkt String karşılaştırma
        if (!product.getOwnerId().equals(userId)) {
            return ApiResponse.error("Bu ürünü silme yetkiniz yok");
        }

        productRepository.delete(product);
        
        return ApiResponse.success("Ürün silindi", null);
    }

    // ✅ ADMIN: Tüm ürünleri listele (müsait olsun olmasın)
    public ApiResponse<List<ProductResponse>> getAllProductsIncludingUnavailable() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responses = products.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    // ✅ ADMIN: İstediği ürünü sil (owner kontrolü yok)
    @Transactional
    public ApiResponse<Void> adminDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElse(null);

        if (product == null) {
            return ApiResponse.error("Ürün bulunamadı");
        }

        productRepository.delete(product);
        
        return ApiResponse.success("Ürün admin tarafından silindi", null);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getImageUrl(),
            product.getCategory(),
            product.getIsAvailable(),
            product.getOwnerId(),
            product.getOwnerName(),
            product.getCreatedAt().format(formatter)
        );
    }
}
