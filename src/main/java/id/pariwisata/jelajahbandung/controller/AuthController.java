package id.pariwisata.jelajahbandung.controller;

import id.pariwisata.jelajahbandung.dto.ApiResponse;
import id.pariwisata.jelajahbandung.dto.AuthResponse;
import id.pariwisata.jelajahbandung.dto.LoginRequest;
import id.pariwisata.jelajahbandung.dto.RegisterRequest;
import id.pariwisata.jelajahbandung.dto.VerifyEmailRequest;
import id.pariwisata.jelajahbandung.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", service.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", service.authenticate(request)));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Object>> verifyEmail(
            @RequestBody VerifyEmailRequest request) {
        service.verifyEmail(request);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully"));
    }
}
