package com.bookstore.bookstore.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT check for public paths
        if (path.equals("/") || path.equals("/api/login") || path.equals("/api/register") ||
            path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);
    System.out.println("=== JWT DEBUG ===");
    System.out.println("Full header: " + authHeader);
    System.out.println("Token: " + token.substring(0, 30) + "...");
    
    String username = jwtUtil.extractUsername(token);
    System.out.println("Username from token: " + username);
    
    if (jwtUtil.validateToken(token, username)) {
        System.out.println("✅ TOKEN VALID - setting auth");
        // your authentication code...
    } else {
        System.out.println("❌ TOKEN INVALID");
    }
    System.out.println("=== END JWT DEBUG ===");
}
}
}