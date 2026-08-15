package com.magrega.demo.service;

import com.magrega.demo.dto.user.*;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public List<AdminUserResponse> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AdminUserResponse createUser(AdminUserCreateRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use.");
        }
        User user = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .phoneNumber(req.phoneNumber())
                .role(req.role())
                .isActive(req.isActive())
                .build();
        return toResponse(userRepo.save(user));
    }

    @Transactional
    public AdminUserResponse updateUser(UUID id, AdminUserUpdateRequest req) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        // uniqueness check must exclude the user's own current row,
        // otherwise editing a user without changing their email would
        // always incorrectly report a conflict against themselves
        userRepo.findByEmail(req.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use.");
                });

        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setPhoneNumber(req.phoneNumber());
        user.setRole(req.role());
        user.setActive(req.isActive());
        if (req.password() != null && !req.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.password()));
        }
        return toResponse(userRepo.save(user));
    }

    @Transactional
    public AdminUserResponse setActive(UUID id, boolean active, UUID currentUserId) {
        if (id.equals(currentUserId) && !active) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot deactivate your own account.");
        }
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        user.setActive(active);
        return toResponse(userRepo.save(user));
    }

    private AdminUserResponse toResponse(User u) {
        return new AdminUserResponse(
                u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(),
                u.getPhoneNumber(), u.getRole(), u.isActive(), u.getCreatedAt()
        );
    }
}
