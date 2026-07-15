package com.nestly.auth.service;

import com.nestly.auth.client.NotificationClient;
import com.nestly.auth.dto.*;
import com.nestly.auth.entity.OtpCode;
import com.nestly.auth.entity.RefreshToken;
import com.nestly.auth.entity.User;
import com.nestly.auth.repository.OtpCodeRepository;
import com.nestly.auth.repository.RefreshTokenRepository;
import com.nestly.auth.repository.UserRepository;
import com.nestly.shared.util.JwtUtil;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final Set<String> VALID_ROLES = Set.of("OWNER", "TENANT", "ADMIN");
    private final RefreshTokenRepository refreshTokenRepository;

    private long refreshTokenExiry = 604800000;


    private final UserRepository userRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final NotificationClient notificationClient;
    private final JwtUtil jwtUtil;
    private final SecureRandom random = new SecureRandom();

    public void requestOtp(String email) {
        String normalized = email.toLowerCase(Locale.ROOT);
        userRepository.findByEmail(normalized).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User Account not found."));

        String code = String.format("%06d", random.nextInt(1_000_000));
        otpCodeRepository.save(OtpCode.builder()
                .email(normalized)
                .code(code)
                .expiresAt(Instant.now().plus(OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES))
                .used(false)
                .createdAt(Instant.now())
                .build());

        try {
            notificationClient.sendOtpEmail(OtpEmailRequest.builder()
                    .email(normalized)
                    .otp(code)
                    .expiryMinutes(OTP_EXPIRY_MINUTES)
                    .build());
        } catch (Exception e) {
            System.out.println("[auth-service] could not send OTP email: " + e.getMessage());
        }
    }

    public AuthResponse verifyOtp(String email, String code) {
        String normalized = email.toLowerCase(Locale.ROOT);
        OtpCode otp = otpCodeRepository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(normalized)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No OTP requested for this email"));

        if (!otp.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired, request a new one");
        }

        otp.setUsed(true);
        otpCodeRepository.save(otp);

        User user = userRepository.findByEmail(normalized).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        String refreshToken = jwtUtil.generateToken(user.getId().toString(), user.getRole(), user.getEmail(), 900000);

        RefreshToken rt = RefreshToken.builder()
                .token(refreshToken)
                .user(userRepository.getReferenceById(user.getId()))
                .expiresAt(LocalDateTime.now().plusSeconds((refreshTokenExiry / 1000)))
                .build();

        refreshTokenRepository.save(rt);

        String token = jwtUtil.generateToken(user.getId().toString(), user.getRole(), user.getEmail(), 604800000);
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public UserResponse createAccount(CreateAccountRequest request) {
        String role = request.getRole().toUpperCase(Locale.ROOT);
        if (!VALID_ROLES.contains(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Role must be one of " + VALID_ROLES);
        }
        String normalized = request.getEmail().toLowerCase(Locale.ROOT);
        if (userRepository.findByEmail(normalized).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists");
        }

        User user = userRepository.save(User.builder()
                .email(normalized)
                .role(role)
                .createdAt(Instant.now())
                .build());

        String name = user.getDisplayName() == null ? user.getEmail() : user.getDisplayName();
        try {
            notificationClient.notify(NotifyRequest.builder()
                    .userId(user.getId().toString())
                    .message("Welcome to Nestly! Your " + role + " account is ready.")
                    .emailSubject("Welcome to Nestly")
                    .emailBody("Hi " + name + ",\n\nAn admin created a " + role
                            + " account for you on Nestly. Log in with this email address"
                            + " - we'll send you a one-time code, no password needed.")
                    .build());
        } catch (Exception e) {
            System.out.println("[auth-service] could not send welcome notification: " + e.getMessage());
        }

        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest,String userId) {
        UUID id = UUID.fromString(userId);
        RefreshToken stored = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token"));
        if (stored.isRevoked()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is already Revoked");
        if (stored.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token has expired");
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found ..Invalid User Id"));

        String newAccessToken = jwtUtil.generateToken(user.getId().toString(), user.getRole(), user.getEmail(), 604800000);
        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(stored.getToken())
                .build();

    }

}
