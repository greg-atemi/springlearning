package com.magrega.demo.service;

import com.magrega.demo.model.Address;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.AddressRepo;
import com.magrega.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    public List<Address> getAddresses()
    {
        return addressRepo.findAll();
    }

    public Address getAddressById(int id)
    {
        return addressRepo.findById(id).orElse(null);
    }

    public Address addAddress(Address address) {
        return addressRepo.save(address);
    }

    public void updateAddressById(Address address) {
        addressRepo.save(address);
    }

    public void deleteAddressById(int id) {
        if (!addressRepo.existsById(id)) {
            throw new RuntimeException("Address with id " + id + " not found");
        }
        addressRepo.deleteById(id);
    }

    public Address assignAddressToUser(UUID userId, Long addressId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Address address = addressRepo.findById(Math.toIntExact(addressId))
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        address.setUser(user);
        user.getAddressList().add(address);

        return addressRepo.save(address);
    }

    public List<Address> getAddressesByUserId(UUID userId) {
        return addressRepo.findByUserId(userId);
    }
}