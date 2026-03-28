package com.magrega.demo.dto.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApprovePaymentDTOTest {

    private ApprovePaymentDTO approvePaymentDTO;

    @BeforeEach
    void setUp() {
        approvePaymentDTO = new ApprovePaymentDTO();
    }

    @Test
    void testSetAndGetOrderId() {
        approvePaymentDTO.setOrderId(200);
        assertEquals(200, approvePaymentDTO.getOrderId());
    }

    @Test
    void testSetAndGetPaidAt() {
        LocalDateTime now = LocalDateTime.now();
        approvePaymentDTO.setPaidAt(now);
        assertEquals(now, approvePaymentDTO.getPaidAt());
    }

    @Test
    void testPaidAtWithSpecificDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 3, 15, 10, 30, 0);
        approvePaymentDTO.setPaidAt(specificTime);
        assertEquals(2025, approvePaymentDTO.getPaidAt().getYear());
        assertEquals(3, approvePaymentDTO.getPaidAt().getMonthValue());
        assertEquals(15, approvePaymentDTO.getPaidAt().getDayOfMonth());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(approvePaymentDTO.getOrderId());
        assertNull(approvePaymentDTO.getPaidAt());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 12, 0);

        ApprovePaymentDTO dto1 = new ApprovePaymentDTO();
        dto1.setOrderId(1);
        dto1.setPaidAt(time);

        ApprovePaymentDTO dto2 = new ApprovePaymentDTO();
        dto2.setOrderId(1);
        dto2.setPaidAt(time);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenPaidAtDiffers() {
        ApprovePaymentDTO dto1 = new ApprovePaymentDTO();
        dto1.setOrderId(1);
        dto1.setPaidAt(LocalDateTime.of(2025, 1, 1, 8, 0));

        ApprovePaymentDTO dto2 = new ApprovePaymentDTO();
        dto2.setOrderId(1);
        dto2.setPaidAt(LocalDateTime.of(2025, 6, 1, 8, 0));

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        approvePaymentDTO.setOrderId(200);
        approvePaymentDTO.setPaidAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        String result = approvePaymentDTO.toString();
        assertTrue(result.contains("200"));
        assertTrue(result.contains("2025"));
    }
}