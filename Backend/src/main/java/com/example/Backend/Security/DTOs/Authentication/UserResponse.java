package com.example.Backend.Security.DTOs.Authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String profilePicture;
    private String bio;
    private String themePreference;
    private long reviewsCount;
}
