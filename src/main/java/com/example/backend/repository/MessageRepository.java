package com.example.backend.repository;

import com.example.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // ✅ Long'dan String'e çevrildi
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(String senderId, String receiverId);
    
    @Query("SELECT m FROM Message m WHERE m.productId = :productId AND " +
           "((m.senderId = :userId1 AND m.receiverId = :userId2) OR " +
           "(m.senderId = :userId2 AND m.receiverId = :userId1)) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(Long productId, String userId1, String userId2);
    
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.receiverId = :userId AND m.isRead = false")
    void markAllAsRead(String userId);
    
    // ✅ Long'dan String'e çevrildi
    Long countByReceiverIdAndIsReadFalse(String receiverId);
}