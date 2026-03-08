package com.magrega.demo.controller;

import com.magrega.demo.model.User;
import com.magrega.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired
    UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id)
    {
        User user = service.getUserById(id);

        if (user != null)
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable int id)
    {
        service.deleteUserById(id);
    }

    @PostMapping("/user")
    public void addCategory(@RequestBody User user)
    {
        service.addUser(user);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody User user)
    {
        service.updateUserById(user);
    }
}
