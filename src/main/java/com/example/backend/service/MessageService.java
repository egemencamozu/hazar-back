package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.entity.Message;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.MessageRepository;
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
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public ApiResponse<MessageResponse> sendMessage(MessageRequest request, String senderId) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(request.getReceiverId()).orElse(null);
        Product product = productRepository.findById(request.getProductId()).orElse(null);

        if (sender == null || receiver == null || product == null) {
            return ApiResponse.error("Geçersiz kullanıcı veya ürün");
        }

        Message message = new Message();
        message.setSenderId(senderId);
        message.setSenderName(sender.getName());
        message.setReceiverId(request.getReceiverId());
        message.setReceiverName(receiver.getName());
        message.setProductId(request.getProductId());
        message.setProductTitle(product.getTitle());
        message.setContent(request.getContent());
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        
        return ApiResponse.success("Mesaj gönderildi", convertToResponse(savedMessage));
    }

    public ApiResponse<List<MessageResponse>> getUserMessages(String userId) {
        List<Message> messages = messageRepository
            .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
        
        List<MessageResponse> responses = messages.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    public ApiResponse<List<MessageResponse>> getConversation(Long productId, String userId1, String userId2) {
        List<Message> messages = messageRepository.findConversation(productId, userId1, userId2);
        
        List<MessageResponse> responses = messages.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success(responses);
    }

    @Transactional
    public ApiResponse<Void> markAsRead(String userId) {
        messageRepository.markAllAsRead(userId);
        return ApiResponse.success("Mesajlar okundu olarak işaretlendi", null);
    }

    public ApiResponse<Long> getUnreadCount(String userId) {
        Long count = messageRepository.countByReceiverIdAndIsReadFalse(userId);
        return ApiResponse.success(count);
    }

    private MessageResponse convertToResponse(Message message) {
        return new MessageResponse(
            message.getId(),
            message.getSenderId(),
            message.getSenderName(),
            message.getReceiverId(),
            message.getReceiverName(),
            message.getProductId(),
            message.getProductTitle(),
            message.getContent(),
            message.getIsRead(),
            message.getCreatedAt().format(formatter)
        );
    }
}
