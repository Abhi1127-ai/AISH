package com.media.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "avatar_expressions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarExpression {

    @Id
    private String id;
    private String emotion;
    private String filePath;
    private String fileName;
    private String contentType;
    private Instant createdAt;
}
