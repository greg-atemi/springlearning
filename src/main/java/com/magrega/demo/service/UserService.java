package com.magrega.demo.service;

import com.magrega.demo.model.User;
import com.magrega.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUserById(UUID id) {
        return userRepo.findById(id).orElse(null);
    }

    public void updateUserById(User user) {
        userRepo.save(user);
    }

    public void deleteUserById(UUID id) {
        userRepo.deleteById(id);
    }
}