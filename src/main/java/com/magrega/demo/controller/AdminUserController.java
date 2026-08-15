package com.magrega.demo.controller;

import com.magrega.demo.dto.user.*;
import com.magrega.demo.model.User;
import com.magrega.demo.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUserResponse> createUser(@Valid @RequestBody AdminUserCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminUserService.createUser(req));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody AdminUserUpdateRequest req) {
        return ResponseEntity.ok(adminUserService.updateUser(id, req));
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<AdminUserResponse> setActive(
            @PathVariable UUID id,
            @RequestParam boolean active,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(adminUserService.setActive(id, active, currentUser.getId()));
    }
}