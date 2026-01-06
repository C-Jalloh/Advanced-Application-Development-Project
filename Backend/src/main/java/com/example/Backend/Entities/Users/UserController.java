package com.example.Backend.Entities.Users;

import com.example.Backend.Security.DTOs.Authentication.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final com.example.Backend.Entities.Reviews.ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String uploadDir = "uploads/";

    public UserController(UserRepository userRepository, com.example.Backend.Entities.Reviews.ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        UserModel user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        long reviewsCount = reviewRepository.countByUser(user);
        return ResponseEntity.ok(new UserResponse(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getRole().name(),
                user.getProfilePicture(),
                user.getBio(),
                user.getThemePreference(),
                reviewsCount
        ));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(Principal principal, @RequestBody ProfileUpdateRequest request) {
        UserModel user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getThemePreference() != null) user.setThemePreference(request.getThemePreference());
        
        userRepository.save(user);
        long reviewsCount = reviewRepository.countByUser(user);
        return ResponseEntity.ok(new UserResponse(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getRole().name(),
                user.getProfilePicture(),
                user.getBio(),
                user.getThemePreference(),
                reviewsCount
        ));
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> updateProfilePicture(Principal principal, @RequestParam("file") MultipartFile file) {
        UserModel user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            // Delete old picture if it exists
            if (user.getProfilePicture() != null) {
                Path oldPath = Paths.get(uploadDir + user.getProfilePicture());
                Files.deleteIfExists(oldPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            
            user.setProfilePicture(fileName);
            userRepository.save(user);
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestParam String oldPassword, @RequestParam String newPassword) {
        UserModel user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("Password changed successfully");
    }
}
