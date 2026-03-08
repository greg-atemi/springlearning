package com.magrega.demo.service;

import com.magrega.demo.model.User;
import com.magrega.demo.repository.UserRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class UserService
{
    @Autowired
    private UserRepo userRepo;

    public List<User> getUsers()
    {
        return userRepo.findAll();
    }

    public User getUserById(int id)
    {
        return userRepo.findById(id).orElse(null);
    }

    public void addUser(User user)
    {
        userRepo.save(user);
    }

    public void updateUserById(User user)
    {
        userRepo.save(user);
    }

    public void deleteUserById(int id)
    {
        userRepo.deleteById(id);
    }
}
