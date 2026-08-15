package com.magrega.demo.dto.user;

import com.magrega.demo.model.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Role role,
        boolean isActive,
        LocalDateTime createdAt
) {}