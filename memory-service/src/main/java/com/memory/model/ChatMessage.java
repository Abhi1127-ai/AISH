package com.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private String userId;
    private String sender; // "user" or "assistant"
    private String content;
    private Instant timestamp;
    private String emotion; // detected user emotion or AI response tone
}
