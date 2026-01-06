package com.example.Backend.Entities.Media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaRepository mediaRepository;
    private final String uploadDir = "uploads/";

    public MediaController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<MediaModel> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), path);

            MediaModel media = new MediaModel();
            media.setFileName(fileName);
            media.setFileType(file.getContentType());
            media.setFilePath(path.toString());
            media.setFileSize(file.getSize());

            return ResponseEntity.ok(mediaRepository.save(media));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        MediaModel media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        try {
            Path path = Paths.get(media.getFilePath());
            byte[] data = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .header("Content-Type", media.getFileType())
                    .body(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
