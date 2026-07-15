package com.nestly.auth.service;

import com.nestly.auth.dto.UpdateProfileRequest;
import com.nestly.auth.dto.UserResponse;
import com.nestly.auth.entity.User;
import com.nestly.auth.repository.UserRepository;
import com.nestly.shared.util.S3PresignHelper;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3PresignHelper s3PresignHelper;

    public UserService(
            UserRepository userRepository,
            S3PresignHelper s3PresignHelper) {

        this.userRepository = userRepository;
        this.s3PresignHelper = s3PresignHelper;
    }

    public UserResponse getUser(String userId) {
        return toResponse(find(userId));
    }

    public UserResponse updateProfile(
            String userId,
            UpdateProfileRequest request) {

        User user = find(userId);

        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }

        if (request.getContact() != null) {
            user.setContact(request.getContact());
        }

        return toResponse(userRepository.save(user));
    }

    public UserResponse uploadProfilePhoto(
            String userId,
            MultipartFile file) {

        User user = find(userId);

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Profile photo cannot be empty"
            );
        }

        String key = s3PresignHelper.presignPut(
                "users/" + userId + "/profile",
                file
        );

        user.setProfilePictureKey(key);

        return toResponse(userRepository.save(user));
    }

    private User find(String userId) {

        UUID id;

        try {
            id = UUID.fromString(userId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid user ID"
            );
        }

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found"
                        )
                );
    }

    private UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .role(user.getRole())
                .displayName(user.getDisplayName())
                .contact(user.getContact())
                .profilePictureUrl(
                        user.getProfilePictureKey() == null
                                ? null
                                : s3PresignHelper.presignGet(
                                user.getProfilePictureKey()
                        )
                )
                .verificationDocUrl(
                        user.getVerificationDocKey() == null
                                ? null
                                : s3PresignHelper.presignGet(
                                user.getVerificationDocKey()
                        )
                )
                .createdAt(user.getCreatedAt())
                .build();
    }
}