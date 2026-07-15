package com.nestly.auth.controller;

import com.nestly.auth.dto.*;
import com.nestly.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
 public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/request-otp")
     public Map<String, String> requestOtp(@Valid @RequestBody RequestOtpRequest request) {
        authService.requestOtp(request.getEmail());
        return Map.of("message", "OTP sent to " + request.getEmail());
    }

    @PostMapping("/verify-otp")
     public AuthResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request.getEmail(), request.getCode());
    }

    @PostMapping("/admin/create-account")
    @ResponseStatus(HttpStatus.CREATED)
     public UserResponse createAccount(
            @Parameter(hidden = true) @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody CreateAccountRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can create accounts");
        }
        return authService.createAccount(request);
    }
    
    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request,@Parameter(hidden = true) @RequestHeader("X-User-Id") String userId)
    {
        return authService.refresh(request,userId);
    }
}
