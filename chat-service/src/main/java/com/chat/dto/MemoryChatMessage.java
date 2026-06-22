package com.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryChatMessage {
    private String id;
    private String userId;
    private String sender;
    private String content;
    private Instant timestamp;
    private String emotion;
}
