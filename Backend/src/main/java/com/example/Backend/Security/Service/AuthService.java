package com.example.Backend.Security.Service;


import com.example.Backend.Entities.Users.UserModel;
import com.example.Backend.Entities.Users.UserRepository;
import com.example.Backend.Security.DTOs.Authentication.AuthRequest;
import com.example.Backend.Security.DTOs.Authentication.AuthResponse;
import com.example.Backend.Security.DTOs.Authentication.UserRegisterRequest;
import com.example.Backend.Security.Utilities.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use!");
        }

        UserModel user = new UserModel();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "User registered successfully!";
    }

    public AuthResponse loginUser(AuthRequest request) {
        Optional<UserModel> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        String token = jwtUtil.generateToken(user.get().getUsername());
        return new AuthResponse(token);
    }
}
