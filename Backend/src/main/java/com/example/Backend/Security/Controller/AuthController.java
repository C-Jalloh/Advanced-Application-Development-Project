package com.example.Backend.Security.Controller;


import com.example.Backend.Security.DTOs.Authentication.AuthRequest;
import com.example.Backend.Security.DTOs.Authentication.AuthResponse;
import com.example.Backend.Security.DTOs.Authentication.UserRegisterRequest;
import com.example.Backend.Security.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }
}
