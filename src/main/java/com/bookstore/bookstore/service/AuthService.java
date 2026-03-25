package com.bookstore.bookstore.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, BCryptPasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // Register user - PREVENTS DUPLICATE EMAILS
    public User register(User user) {
        // Check if email already exists
        if (userRepo.existsByEmailIgnoreCase(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole("CUSTOMER");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Regular customer login
    public String login(String email, String password) {
        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    // Admin login (only ADMIN role users)
    public String adminLogin(String email, String password) {
        User admin = userRepo.findByEmailIgnoreCase(email)
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!encoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid admin password");
        }
        return jwtUtil.generateToken(email);
    }
}