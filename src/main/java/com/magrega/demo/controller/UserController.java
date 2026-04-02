package com.magrega.demo.controller;

import com.magrega.demo.dto.address.AddressDTO;
import com.magrega.demo.model.Address;
import com.magrega.demo.service.AddressService;
import com.magrega.demo.model.User;
import com.magrega.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController
{
    @Autowired
    UserService service;

    @Autowired
    AddressService addressService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id)
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
    public void deleteUserById(@PathVariable UUID id)
    {
        service.deleteUserById(id);
    }

    @PostMapping("/user")
    public void addUser(@RequestBody User user)
    {
        service.addUser(user);
    }

    @PostMapping("/user/{id}/address")
    public Address addAddress(
            @PathVariable UUID id,
            @RequestBody AddressDTO dto) {

        return addressService.addAddressToUser(id, dto);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody User user)
    {
        service.updateUserById(user);
    }
}
