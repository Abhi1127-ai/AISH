package com.media.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class MediaStorageService {

    private final Path uploadLocation;

    public MediaStorageService(@Value("${app.upload.dir:uploads/avatar}") String uploadDir) {
        this.uploadLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the upload directory: " + uploadDir, e);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String cleanFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + cleanFileName);
            }
            
            Path targetLocation = this.uploadLocation.resolve(cleanFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + cleanFileName, e);
        }
    }

    public void deleteFile(String filePathString) {
        try {
            Path filePath = Paths.get(filePathString);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log warning, do not block database delete if file delete fails
            System.err.println("Warning: Failed to delete physical file: " + filePathString + ". Error: " + e.getMessage());
        }
    }
}
