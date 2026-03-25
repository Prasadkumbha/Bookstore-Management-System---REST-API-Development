package com.bookstore.bookstore.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  // ← ADD
import org.springframework.web.bind.annotation.RestController;         // ← ADD

import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.security.JwtUtil;
import com.bookstore.bookstore.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService service;
    private final UserRepository userRepo;     // ← ADD
    private final JwtUtil jwtUtil;             // ← ADD

    public AuthController(AuthService service, UserRepository userRepo, JwtUtil jwtUtil) {
        this.service = service;
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        System.out.println("BEFORE save - email: '" + user.getEmail() + "'");
        User saved = service.register(user);
        System.out.println("AFTER save - ID: " + saved.getId() + ", email: '" + saved.getEmail() + "'");
        return saved;
    }

    @PostMapping("/login") 
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println("Login attempt: " + user.getEmail() + ", name: " + user.getName());
        
        try {
            String token = service.login(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            System.out.println("Login error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String username = jwtUtil.extractUsername(token);  // ← Now works
        
        if (username != null && jwtUtil.validateToken(token, username)) {
            String newToken = jwtUtil.generateToken(username);
            return ResponseEntity.ok(Map.of("token", newToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody User user) {
        try {
            String token = service.adminLogin(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(Map.of("token", token, "role", "ADMIN"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Admin credentials invalid"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByEmailIgnoreCase(username).orElse(null);  // ← Now works
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(Map.of(
            "email", user.getEmail(),
            "role", user.getRole(),
            "name", user.getName()
        ));
    }
}