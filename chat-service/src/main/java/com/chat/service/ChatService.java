package com.chat.service;

import com.chat.dto.ChatRequest;
import com.chat.dto.ChatResponse;
import com.chat.dto.MemoryChatMessage;
import com.chat.dto.MemoryUserFact;
import com.chat.dto.GeminiRequest;
import com.chat.dto.GeminiRequest.Content;
import com.chat.dto.GeminiRequest.Part;
import com.chat.dto.GeminiRequest.GenerationConfig;
import com.chat.dto.GeminiRequest.ResponseSchema;
import com.chat.dto.GeminiRequest.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RestClient restClient;
    private final String memoryServiceBase = "http://localhost:8083/api/memory";
    private final String mediaServiceBase = "http://localhost:8084/api/media";
    private final String geminiApiKey = "${gemini.api.key}";
    private final String geminiUrl = "${gemini.api.url}";

    /**
     * Orchestrates a chat interaction:
     * 1. Pull recent chat history and user facts from Memory Service.
     * 2. Build a Gemini request that includes the user message, recent history and facts.
     * 3. Call Gemini API and obtain a structured response (reply, emotion, optional new facts).
     * 4. Persist the reply as a new ChatMessage and any new facts into Memory Service.
     * 5. Optionally retrieve an avatar URL from Media Service based on the emotion.
     */
    public ChatResponse processChat(ChatRequest request) {
        // 1. Retrieve recent chats
        String chatsUrl = UriComponentsBuilder.fromHttpUrl(memoryServiceBase + "/chats")
                .queryParam("userId", request.getUserId())
                .queryParam("limit", 20)
                .toUriString();
        List<MemoryChatMessage> recentChats = restClient.get()
                .uri(chatsUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<List<MemoryChatMessage>>() {});

        // 2. Retrieve user facts
        String factsUrl = UriComponentsBuilder.fromHttpUrl(memoryServiceBase + "/facts")
                .queryParam("userId", request.getUserId())
                .toUriString();
        List<MemoryUserFact> userFacts = restClient.get()
                .uri(factsUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<List<MemoryUserFact>>() {});

        // 3. Build Gemini request payload
        StringBuilder contextBuilder = new StringBuilder();
        if (recentChats != null) {
            for (MemoryChatMessage cm : recentChats) {
                contextBuilder.append(cm.getSender()).append(": ").append(cm.getContent()).append("\n");
            }
        }
        if (userFacts != null) {
            contextBuilder.append("Facts:\n");
            for (MemoryUserFact f : userFacts) {
                contextBuilder.append("- ").append(f.getFact()).append("\n");
            }
        }
        String fullPrompt = request.getMessage() + "\n\nContext:\n" + contextBuilder.toString();

        Content content = Content.builder()
                .parts(List.of(Part.builder().text(fullPrompt).build()))
                .build();
        GenerationConfig genConfig = GenerationConfig.builder()
                .responseMimeType("application/json")
                .responseSchema(ResponseSchema.builder()
                        .type("object")
                        .properties(Map.of(
                                "reply", Property.builder().type("string").description("Chatbot reply").build(),
                                "emotion", Property.builder().type("string").description("Emotion like happy, sad, etc.").build(),
                                "facts", Property.builder().type("array").items(Property.builder().type("string").description("New fact").build()).description("Optional new facts").build()
                        ))
                        .required(List.of("reply", "emotion"))
                        .build())
                .build();
        GeminiRequest geminiRequest = GeminiRequest.builder()
                .contents(List.of(content))
                .generationConfig(genConfig)
                .build();

        // 4. Call Gemini API
        String geminiFullUrl = UriComponentsBuilder.fromHttpUrl(geminiUrl)
                .queryParam("key", geminiApiKey)
                .toUriString();
        Map<String, Object> geminiResponse = restClient.post()
                .uri(geminiFullUrl)
                .body(geminiRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        // Extract fields (assuming Gemini respects the schema)
        String reply = (String) geminiResponse.getOrDefault("reply", "");
        String emotion = (String) geminiResponse.getOrDefault("emotion", "neutral");
        List<String> newFacts = (List<String>) geminiResponse.getOrDefault("facts", List.of());

        // 5. Persist new facts
        if (newFacts != null && !newFacts.isEmpty()) {
            for (String factText : newFacts) {
                MemoryUserFact fact = MemoryUserFact.builder()
                        .userId(request.getUserId())
                        .fact(factText)
                        .build();
                restClient.post()
                        .uri(memoryServiceBase + "/facts")
                        .body(fact)
                        .retrieve()
                        .toBodilessEntity();
            }
        }

        // 6. Persist chat reply
        MemoryChatMessage replyMessage = MemoryChatMessage.builder()
                .userId(request.getUserId())
                .sender("assistant")
                .content(reply)
                .emotion(emotion)
                .build();
        restClient.post()
                .uri(memoryServiceBase + "/chats")
                .body(replyMessage)
                .retrieve()
                .toBodilessEntity();

        // 7. Construct avatar URL based on emotion using Media Service's expression endpoint
        String avatarUrl = mediaServiceBase + "/expressions/" + emotion;
        // No need to call Media Service now; the client can retrieve the image from this URL.

        return ChatResponse.builder()
                .reply(reply)
                .emotion(emotion)
                .avatarUrl(avatarUrl)
                .build();
    }
}
