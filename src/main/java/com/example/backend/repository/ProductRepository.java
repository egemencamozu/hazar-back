package com.example.backend.repository;

import com.example.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // ✅ Long'dan String'e çevrildi
    List<Product> findByOwnerId(String ownerId);
    List<Product> findByIsAvailableTrue();
    List<Product> findByCategoryAndIsAvailableTrue(String category);
    List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title, String description
    );
}