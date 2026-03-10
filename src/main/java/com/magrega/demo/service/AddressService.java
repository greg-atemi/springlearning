package com.magrega.demo.service;

import com.magrega.demo.model.Address;
import com.magrega.demo.repository.AddressRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class AddressService
{
    @Autowired
    private AddressRepo addressRepo;

    public List<Address> getAddresses()
    {
        return addressRepo.findAll();
    }

    public Address getAddressById(int id)
    {
        return addressRepo.findById(id).orElse(null);
    }

    public void addAddress(Address Address)
    {
        addressRepo.save(Address);
    }

    public void updateAddressById(Address Address)
    {
        addressRepo.save(Address);
    }

    public void deleteAddressById(int id) {
        if (!addressRepo.existsById(id)) {
            throw new RuntimeException("Address with id " + id + " not found");
        }
        addressRepo.deleteById(id);
    }
}
