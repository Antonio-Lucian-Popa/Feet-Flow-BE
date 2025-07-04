package com.asusoftware.feet_flow_api.post.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class MediaStorageService {

    @Value("${external-link.url}")
    private String externalLinkBase;

    /**
     * Simulează un upload local. În producție: folosește S3 / Cloudinary.
     */
    public String upload(MultipartFile file, UUID postId) {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file not allowed");
        try {
            String originalFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path directory = Paths.get("uploads/images", postId.toString()).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            Path destination = directory.resolve(originalFilename);

            if (!destination.getParent().equals(directory)) {
                throw new SecurityException("Invalid path detected");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return externalLinkBase + postId + "/" + originalFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public void delete(String url) {
        if (url == null || url.isBlank()) return;

        try {
            // Extragem partea după domain, ex: uploads/images/{postId}/{filename}
            String relativePath = url.replace(externalLinkBase, "");
            Path filePath = Paths.get("uploads").resolve(relativePath).toAbsolutePath().normalize();

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", url, e);
        }
    }

}
