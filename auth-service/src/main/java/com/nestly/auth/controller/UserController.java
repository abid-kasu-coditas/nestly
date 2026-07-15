package com.nestly.auth.controller;

import com.nestly.auth.dto.*;
import com.nestly.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
     public UserResponse me(@Parameter(hidden = true) @RequestHeader("X-User-Id") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/update-profile")
     public UserResponse updateMe(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(userId, request);
    }

    @PostMapping("/upload-profile-photo")
     public SingleResponse presign(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody PresignRequest request) {
        return userService.presign(userId, request);
    }

    @GetMapping("/{id}")
     public UserResponse getById(@PathVariable String id) {
        return userService.getUser(id);
    }

}
