package com.asusoftware.feet_flow_api.post.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class MediaStorageService {

    private final String UPLOAD_DIR = "uploads";

    /**
     * Simulează un upload local. În producție: folosește S3 / Cloudinary.
     */
    public String upload(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            // În producție, returnează URL complet (ex: https://cdn.onlyfeet.com/...)
            return "/uploads/" + filename;
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Upload failed");
        }
    }

    public void delete(String url) {
        if (url == null || url.isBlank()) return;

        try {
            String filename = Paths.get(url).getFileName().toString();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", url);
        }
    }
}
