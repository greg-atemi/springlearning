package com.magrega.demo.service;

import com.magrega.demo.model.User;
import com.magrega.demo.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("johndoe@example.com");
    }

    // ─────────────────────────────────────────────
    // getUsers()
    // ─────────────────────────────────────────────

    @Test
    void getUsers_ShouldReturnAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(mockUser));

        List<User> result = userService.getUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
        verify(userRepo, times(1)).findAll();
    }

    @Test
    void getUsers_ShouldReturnEmptyList_WhenNoUsers() {
        when(userRepo.findAll()).thenReturn(List.of());

        List<User> result = userService.getUsers();

        assertThat(result).isEmpty();
        verify(userRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getUserById()
    // ─────────────────────────────────────────────

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        when(userRepo.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThat(result.getEmail()).isEqualTo("johndoe@example.com");
        verify(userRepo, times(1)).findById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void getUserById_ShouldReturnNull_WhenNotFound() {
        when(userRepo.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.empty());

        User result = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertThat(result).isNull();
        verify(userRepo, times(1)).findById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    // ─────────────────────────────────────────────
    // updateUserById()
    // ─────────────────────────────────────────────

    @Test
    void updateUserById_ShouldCallSave_WithGivenUser() {
        userService.updateUserById(mockUser);

        verify(userRepo, times(1)).save(mockUser);
    }

    @Test
    void updateUserById_ShouldPersistChangedFields() {
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("johndoe@example.com");

        userService.updateUserById(mockUser);

        verify(userRepo).save(argThat(u ->
                u.getFirstName().equals("John") &&
                    u.getLastName().equals("Doe") &&
                        u.getEmail().equals("johndoe@example.com")
        ));
    }

    // ─────────────────────────────────────────────
    // deleteUserById()
    // ─────────────────────────────────────────────

    @Test
    void deleteUserById_ShouldCallDeleteById_WithCorrectId() {
        userService.deleteUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        verify(userRepo, times(1)).deleteById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void deleteUserById_ShouldNotCallFindOrSave() {
        userService.deleteUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        verify(userRepo, never()).findById(any());
        verify(userRepo, never()).save(any());
    }
}