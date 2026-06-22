package com.media.controller;

import com.media.model.AvatarExpression;
import com.media.repository.AvatarExpressionRepository;
import com.media.service.MediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final AvatarExpressionRepository avatarExpressionRepository;
    private final MediaStorageService mediaStorageService;

    @PostMapping("/upload")
    public ResponseEntity<AvatarExpression> uploadExpression(
            @RequestParam("file") MultipartFile file,
            @RequestParam("emotion") String emotion) {
        
        String filePath = mediaStorageService.storeFile(file);
        
        AvatarExpression expression = AvatarExpression.builder()
                .emotion(emotion.toLowerCase().trim())
                .filePath(filePath)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .createdAt(Instant.now())
                .build();
                
        AvatarExpression saved = avatarExpressionRepository.save(expression);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/expressions/{emotion}")
    public ResponseEntity<Resource> getExpressionImage(@PathVariable String emotion) {
        String searchEmotion = emotion.toLowerCase().trim();
        Optional<AvatarExpression> expressionOpt = avatarExpressionRepository
                .findFirstByEmotionOrderByCreatedAtDesc(searchEmotion);
                
        // Fallback to neutral if requested emotion is not found
        if (expressionOpt.isEmpty() && !searchEmotion.equals("neutral")) {
            expressionOpt = avatarExpressionRepository.findFirstByEmotionOrderByCreatedAtDesc("neutral");
        }
        
        if (expressionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        AvatarExpression expression = expressionOpt.get();
        File file = new File(expression.getFilePath());
        
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (expression.getContentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(expression.getContentType());
            } catch (Exception e) {
                // Keep octet-stream
            }
        }
        
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/expressions")
    public ResponseEntity<List<AvatarExpression>> listExpressions() {
        return ResponseEntity.ok(avatarExpressionRepository.findAll());
    }

    @DeleteMapping("/expressions/{id}")
    public ResponseEntity<Void> deleteExpression(@PathVariable String id) {
        return avatarExpressionRepository.findById(id)
                .map(expression -> {
                    mediaStorageService.deleteFile(expression.getFilePath());
                    avatarExpressionRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
