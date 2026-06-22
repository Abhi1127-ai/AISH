package com.memory.service;

import com.memory.model.ChatMessage;
import com.memory.model.UserFact;
import com.memory.repository.ChatMessageRepository;
import com.memory.repository.UserFactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserFactRepository userFactRepository;

    public ChatMessage saveChatMessage(ChatMessage message) {
        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getRecentChatMessages(String userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messages = chatMessageRepository.findByUserId(userId, pageable);
        
        // Return in chronological order (oldest to newest)
        List<ChatMessage> chronologicalMessages = new ArrayList<>(messages);
        Collections.reverse(chronologicalMessages);
        return chronologicalMessages;
    }

    public UserFact saveUserFact(UserFact fact) {
        if (fact.getCreatedAt() == null) {
            fact.setCreatedAt(Instant.now());
        }
        return userFactRepository.save(fact);
    }

    public List<UserFact> getUserFacts(String userId) {
        return userFactRepository.findByUserId(userId);
    }

    public void deleteUserFact(String id) {
        userFactRepository.deleteById(id);
    }

    public String getMemoryContextString(String userId) {
        List<UserFact> facts = userFactRepository.findByUserId(userId);
        if (facts.isEmpty()) {
            return "No historical facts known yet about the user.";
        }
        return facts.stream()
                .map(UserFact::getFact)
                .map(fact -> "- " + fact)
                .collect(Collectors.joining("\n", "Key facts about the user:\n", ""));
    }
}
