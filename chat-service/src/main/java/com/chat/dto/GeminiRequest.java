package com.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerationConfig {
        private String responseMimeType;
        private ResponseSchema responseSchema;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseSchema {
        private String type;
        private Map<String, Property> properties;
        private List<String> required;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {
        private String type;
        private String description;
        private Property items; // for array type
    }
}
