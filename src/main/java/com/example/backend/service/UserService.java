package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.User;
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
public class UserService {
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest request) {
        // Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Bu email adresi zaten kullanılıyor");
        }

        // Yeni kullanıcı oluştur
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Hash'lenecek (Spring Security ile)
        user.setPhone(request.getPhone());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        
        return ApiResponse.success("Kayıt başarılı", convertToResponse(savedUser));
    }

    public ApiResponse<UserResponse> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElse(null);

        if (user == null) {
            return ApiResponse.error("Email veya şifre hatalı");
        }

        // Şifre kontrolü (TODO: BCrypt ile hash kontrolü yapılacak)
        if (!user.getPassword().equals(request.getPassword())) {
            return ApiResponse.error("Email veya şifre hatalı");
        }

        return ApiResponse.success("Giriş başarılı", convertToResponse(user));
    }

    public ApiResponse<UserResponse> getUserById(String id) {
        User user = userRepository.findById(id)
            .orElse(null);

        if (user == null) {
            return ApiResponse.error("Kullanıcı bulunamadı");
        }

        return ApiResponse.success(convertToResponse(user));
    }

    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = users.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getIsAdmin() != null ? user.getIsAdmin() : false, // ✅ Admin flag eklendi
            user.getCreatedAt().format(formatter)
        );
    }
}
