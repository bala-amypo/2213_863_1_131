package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.AppUser;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserService appUserService, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AuthRequest request) {
        // Assume default role or extract from request if DTO allowed. 
        // Prompt DTO "AuthRequest" only has email/password.
        // So we default role to "USER".
        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role("USER")
                .active(true)
                .build();
        return ResponseEntity.ok(appUserService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Authenticate logic. 
        // Since we didn't inject AuthenticationManager (requires config), we can do manual check?
        // Or configure AuthenticationManager in SecurityConfig. 
        // Standard Spring Security JWT usually uses AuthenticationManager.
        // However, I can manually check password using PasswordEncoder.
        AppUser user = appUserService.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
             throw new RuntimeException("Invalid credentials"); // Or BadCredentialsException
        }

        String token = tokenProvider.createToken(user);
        
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }
}
