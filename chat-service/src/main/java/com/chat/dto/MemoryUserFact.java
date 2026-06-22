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
public class MemoryUserFact {
    private String id;
    private String userId;
    private String fact;
    private Instant createdAt;
}
