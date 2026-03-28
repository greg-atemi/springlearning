package com.magrega.demo.dto.payment;

import com.magrega.demo.model.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreatePaymentDTOTest {

    private CreatePaymentDTO createPaymentDTO;

    @BeforeEach
    void setUp() {
        createPaymentDTO = new CreatePaymentDTO();
    }

    @Test
    void testSetAndGetOrderId() {
        createPaymentDTO.setOrderId(300);
        assertEquals(300, createPaymentDTO.getOrderId());
    }

    @Test
    void testSetAndGetAmount() {
        BigDecimal amount = new BigDecimal("1500.75");
        createPaymentDTO.setAmount(amount);
        assertEquals(new BigDecimal("1500.75"), createPaymentDTO.getAmount());
    }

    @Test
    void testSetAndGetTransactionReference() {
        createPaymentDTO.setTransactionReference("TXN-2025-XYZ");
        assertEquals("TXN-2025-XYZ", createPaymentDTO.getTransactionReference());
    }

    @Test
    void testSetAndGetPaymentMethod() {
        createPaymentDTO.setPaymentMethod(PaymentMethod.MPESA);
        assertEquals(PaymentMethod.MPESA, createPaymentDTO.getPaymentMethod());
    }

    @Test
    void testAllPaymentMethodValues() {
        for (PaymentMethod method : PaymentMethod.values()) {
            createPaymentDTO.setPaymentMethod(method);
            assertEquals(method, createPaymentDTO.getPaymentMethod());
        }
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(createPaymentDTO.getOrderId());
        assertNull(createPaymentDTO.getAmount());
        assertNull(createPaymentDTO.getTransactionReference());
        assertNull(createPaymentDTO.getPaymentMethod());
    }

    @Test
    void testAmountWithZeroValue() {
        createPaymentDTO.setAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, createPaymentDTO.getAmount());
    }

    @Test
    void testAmountWithLargeValue() {
        BigDecimal largeAmount = new BigDecimal("9999999.99");
        createPaymentDTO.setAmount(largeAmount);
        assertEquals(largeAmount, createPaymentDTO.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        CreatePaymentDTO dto1 = new CreatePaymentDTO();
        dto1.setOrderId(1);
        dto1.setAmount(new BigDecimal("500.00"));
        dto1.setTransactionReference("REF001");
        dto1.setPaymentMethod(PaymentMethod.MPESA);

        CreatePaymentDTO dto2 = new CreatePaymentDTO();
        dto2.setOrderId(1);
        dto2.setAmount(new BigDecimal("500.00"));
        dto2.setTransactionReference("REF001");
        dto2.setPaymentMethod(PaymentMethod.MPESA);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenAmountDiffers() {
        CreatePaymentDTO dto1 = new CreatePaymentDTO();
        dto1.setAmount(new BigDecimal("100.00"));

        CreatePaymentDTO dto2 = new CreatePaymentDTO();
        dto2.setAmount(new BigDecimal("200.00"));

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        createPaymentDTO.setOrderId(300);
        createPaymentDTO.setTransactionReference("TXN-ABC");
        String result = createPaymentDTO.toString();
        assertTrue(result.contains("300"));
        assertTrue(result.contains("TXN-ABC"));
    }
}