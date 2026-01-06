package com.example.Backend.Entities.Users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String email;
    private String bio;
    private String themePreference;
}
