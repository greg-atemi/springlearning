package com.magrega.demo.controller;

import com.magrega.demo.model.Address;
import com.magrega.demo.model.User;
import com.magrega.demo.service.AddressService;
import com.magrega.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAddresses() {
        return new ResponseEntity<>(addressService.getAddresses(), HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable int id) {
        Address address = addressService.getAddressById(id);
        if (address != null) {
            return new ResponseEntity<>(address, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/address/{id}")
    public void deleteAddressById(@PathVariable int id) {
        addressService.deleteAddressById(id);
    }

//    @PostMapping("/address")
//    public void addAddress(@RequestBody Address address) {
//        addressService.addAddress(address);
//    }

    @PostMapping("/address")
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        Address saved = addressService.addAddress(address);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/address/{addressId}/assign")
    public Address assignAddressToUser(
            @PathVariable Long addressId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        return addressService.assignAddressToUser(user.getId(), addressId);
    }

    @GetMapping("/address/my")
    public ResponseEntity<List<Address>> getMyAddresses(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByEmail(userDetails.getUsername());
        List<Address> addresses = addressService.getAddressesByUserId(user.getId());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PutMapping("/address")
    public void updateAddress(@RequestBody Address address) {
        addressService.updateAddressById(address);
    }
}