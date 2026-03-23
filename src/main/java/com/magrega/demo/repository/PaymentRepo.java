package com.magrega.demo.repository;

import com.magrega.demo.model.Order;
import com.magrega.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer>
{
    Optional<Payment> findByOrder(Order order);
}
