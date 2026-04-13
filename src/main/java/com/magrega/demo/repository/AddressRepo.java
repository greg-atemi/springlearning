package com.magrega.demo.repository;

import com.magrega.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer>
{
    List<Address> findByUserId(UUID userId);
}
