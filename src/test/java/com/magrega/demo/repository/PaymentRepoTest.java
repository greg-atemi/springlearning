package com.magrega.demo.repository;

import com.magrega.demo.model.*;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentMethod;
import com.magrega.demo.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PaymentRepoTest {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private OrderRepo orderRepo;

    private Order savedOrder;
    private Payment savedPayment;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setFirstName("Greg");
        user.setLastName("Atemi");
        user.setEmail("greg@systechafrica.com");
        user.setAddressList(new ArrayList<>());
        userRepo.save(user);

        Address address = new Address();
        address.setCountry("Kenya");
        address.setCounty("Nairobi");
        address.setLocality("Westlands");
        address.setMapsPin("-1.2921,36.8219");
        address.setUser(user);
        addressRepo.save(address);

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setTotalAmount(new BigDecimal("1999.98"));
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setItems(new ArrayList<>());
        savedOrder = orderRepo.save(order);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(new BigDecimal("1999.98"));
        payment.setPaymentMethod(PaymentMethod.MPESA);
        payment.setTransactionReference("TXN-001");
        payment.setPaidAt(LocalDateTime.now());
        savedPayment = paymentRepo.save(payment);
    }

    // ─────────────────────────────────────────────
    // findByOrder()
    // ─────────────────────────────────────────────

    @Test
    void findByOrder_ShouldReturnPayment_WhenOrderExists() {
        Optional<Payment> result = paymentRepo.findByOrder(savedOrder);

        assertThat(result).isPresent();
        assertThat(result.get().getTransactionReference()).isEqualTo("TXN-001");
        assertThat(result.get().getAmount()).isEqualByComparingTo("1999.98");
    }

    @Test
    void findByOrder_ShouldReturnEmpty_WhenNoPaymentForOrder() {
        Order unpaidOrder = new Order();
        unpaidOrder.setUser(savedOrder.getUser());
        unpaidOrder.setAddress(savedOrder.getAddress());
        unpaidOrder.setTotalAmount(new BigDecimal("500.00"));
        unpaidOrder.setOrderStatus(OrderStatus.PENDING);
        unpaidOrder.setPaymentStatus(PaymentStatus.PENDING);
        unpaidOrder.setItems(new ArrayList<>());
        Order savedUnpaidOrder = orderRepo.save(unpaidOrder);

        Optional<Payment> result = paymentRepo.findByOrder(savedUnpaidOrder);

        assertThat(result).isEmpty();
    }

    @Test
    void findByOrder_ShouldReturnCorrectPayment_WhenMultiplePaymentsExist() {
        Order order2 = new Order();
        order2.setUser(savedOrder.getUser());
        order2.setAddress(savedOrder.getAddress());
        order2.setTotalAmount(new BigDecimal("500.00"));
        order2.setOrderStatus(OrderStatus.PENDING);
        order2.setPaymentStatus(PaymentStatus.PENDING);
        order2.setItems(new ArrayList<>());
        Order savedOrder2 = orderRepo.save(order2);

        Payment payment2 = new Payment();
        payment2.setOrder(savedOrder2);
        payment2.setAmount(new BigDecimal("500.00"));
        payment2.setPaymentMethod(PaymentMethod.MPESA);
        payment2.setTransactionReference("TXN-002");
        payment2.setPaidAt(LocalDateTime.now());
        paymentRepo.save(payment2);

        Optional<Payment> result1 = paymentRepo.findByOrder(savedOrder);
        Optional<Payment> result2 = paymentRepo.findByOrder(savedOrder2);

        assertThat(result1.get().getTransactionReference()).isEqualTo("TXN-001");
        assertThat(result2.get().getTransactionReference()).isEqualTo("TXN-002");
    }
}