package com.example.userservice.controller;

import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "API for check invites")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/currentUser")
    public ResponseEntity<String> getCurrentUsername() {
        try {
            String currentUsername = userService.getCurrentUsername();
            return ResponseEntity.ok(currentUsername);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }
}
