package com.memory.controller;

import com.memory.model.ChatMessage;
import com.memory.model.UserFact;
import com.memory.service.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memory")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping("/chats")
    public ResponseEntity<ChatMessage> saveChatMessage(@RequestBody ChatMessage message) {
        return ResponseEntity.ok(memoryService.saveChatMessage(message));
    }

    @GetMapping("/chats")
    public ResponseEntity<List<ChatMessage>> getRecentChats(
            @RequestParam String userId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(memoryService.getRecentChatMessages(userId, limit));
    }

    @PostMapping("/facts")
    public ResponseEntity<UserFact> saveUserFact(@RequestBody UserFact fact) {
        return ResponseEntity.ok(memoryService.saveUserFact(fact));
    }

    @GetMapping("/facts")
    public ResponseEntity<List<UserFact>> getUserFacts(@RequestParam String userId) {
        return ResponseEntity.ok(memoryService.getUserFacts(userId));
    }

    @DeleteMapping("/facts/{id}")
    public ResponseEntity<Void> deleteUserFact(@PathVariable String id) {
        memoryService.deleteUserFact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/context")
    public ResponseEntity<String> getMemoryContext(@RequestParam String userId) {
        return ResponseEntity.ok(memoryService.getMemoryContextString(userId));
    }
}
