package com.magrega.demo.dto.order;

import com.magrega.demo.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOrderStatusDTOTest {

    private UpdateOrderStatusDTO updateOrderStatusDTO;

    @BeforeEach
    void setUp() {
        updateOrderStatusDTO = new UpdateOrderStatusDTO();
    }

    @Test
    void testSetAndGetOrderId() {
        updateOrderStatusDTO.setOrderId(101);
        assertEquals(101, updateOrderStatusDTO.getOrderId());
    }

    @Test
    void testSetAndGetOrderStatus() {
        updateOrderStatusDTO.setOrderStatus(OrderStatus.PROCESSING);
        assertEquals(OrderStatus.PROCESSING, updateOrderStatusDTO.getOrderStatus());
    }

    @Test
    void testAllOrderStatusValues() {
        for (OrderStatus status : OrderStatus.values()) {
            updateOrderStatusDTO.setOrderStatus(status);
            assertEquals(status, updateOrderStatusDTO.getOrderStatus());
        }
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(updateOrderStatusDTO.getOrderId());
        assertNull(updateOrderStatusDTO.getOrderStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateOrderStatusDTO dto1 = new UpdateOrderStatusDTO();
        dto1.setOrderId(1);
        dto1.setOrderStatus(OrderStatus.PROCESSING);

        UpdateOrderStatusDTO dto2 = new UpdateOrderStatusDTO();
        dto2.setOrderId(1);
        dto2.setOrderStatus(OrderStatus.PROCESSING);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenStatusDiffers() {
        UpdateOrderStatusDTO dto1 = new UpdateOrderStatusDTO();
        dto1.setOrderId(1);
        dto1.setOrderStatus(OrderStatus.PROCESSING);

        UpdateOrderStatusDTO dto2 = new UpdateOrderStatusDTO();
        dto2.setOrderId(1);
        dto2.setOrderStatus(OrderStatus.CANCELLED);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        updateOrderStatusDTO.setOrderId(99);
        updateOrderStatusDTO.setOrderStatus(OrderStatus.PROCESSING);
        String result = updateOrderStatusDTO.toString();
        assertTrue(result.contains("99"));
        assertTrue(result.contains("PROCESSING"));
    }
}