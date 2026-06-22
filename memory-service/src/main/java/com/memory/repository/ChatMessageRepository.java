package com.memory.repository;

import com.memory.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByUserId(String userId, Pageable pageable);
    List<ChatMessage> findByUserIdOrderByTimestampDesc(String userId);
}
