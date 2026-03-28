package com.magrega.demo.dto.orderItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderItemDTOTest {

    private CreateOrderItemDTO createOrderItemDTO;

    @BeforeEach
    void setUp() {
        createOrderItemDTO = new CreateOrderItemDTO();
    }

    @Test
    void testSetAndGetOrderId() {
        createOrderItemDTO.setOrderId(10);
        assertEquals(10, createOrderItemDTO.getOrderId());
    }

    @Test
    void testSetAndGetQuantity() {
        createOrderItemDTO.setQuantity(3);
        assertEquals(3, createOrderItemDTO.getQuantity());
    }

    @Test
    void testSetAndGetProductId() {
        createOrderItemDTO.setProductId(55);
        assertEquals(55, createOrderItemDTO.getProductId());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(createOrderItemDTO.getOrderId());
        assertNull(createOrderItemDTO.getQuantity());
        assertNull(createOrderItemDTO.getProductId());
    }

    @Test
    void testQuantityOfOne() {
        createOrderItemDTO.setQuantity(1);
        assertEquals(1, createOrderItemDTO.getQuantity());
    }

    @Test
    void testLargeQuantity() {
        createOrderItemDTO.setQuantity(10000);
        assertEquals(10000, createOrderItemDTO.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateOrderItemDTO dto1 = new CreateOrderItemDTO();
        dto1.setOrderId(1);
        dto1.setQuantity(2);
        dto1.setProductId(3);

        CreateOrderItemDTO dto2 = new CreateOrderItemDTO();
        dto2.setOrderId(1);
        dto2.setQuantity(2);
        dto2.setProductId(3);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenQuantityDiffers() {
        CreateOrderItemDTO dto1 = new CreateOrderItemDTO();
        dto1.setQuantity(2);

        CreateOrderItemDTO dto2 = new CreateOrderItemDTO();
        dto2.setQuantity(5);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        createOrderItemDTO.setOrderId(10);
        createOrderItemDTO.setQuantity(2);
        createOrderItemDTO.setProductId(55);
        String result = createOrderItemDTO.toString();
        assertTrue(result.contains("10"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("55"));
    }
}