package com.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "user_facts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFact {
    @Id
    private String id;
    private String userId;
    private String fact;
    private Instant createdAt;
}
