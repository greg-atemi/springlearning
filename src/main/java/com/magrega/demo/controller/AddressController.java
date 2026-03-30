package com.magrega.demo.controller;

import com.magrega.demo.model.Address;
import com.magrega.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class AddressController
{
    @Autowired
    AddressService service;

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddresses(){
        return new ResponseEntity<>(service.getAddresses(), HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable int id)
    {
        Address address = service.getAddressById(id);

        if (address != null)
        {
            return new ResponseEntity<>(address, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/address/{id}")
    public void deleteAddressById(@PathVariable int id)
    {
        service.deleteAddressById(id);
    }

    @PostMapping("/address")
    public void addAddress(@RequestBody Address address)
    {
        service.addAddress(address);
    }

    @PutMapping("/address")
    public void updateAddress(@RequestBody Address address)
    {
        service.updateAddressById(address);
    }
}
