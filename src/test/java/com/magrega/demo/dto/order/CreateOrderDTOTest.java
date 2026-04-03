package com.magrega.demo.dto.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderDTOTest {

    private CreateOrderDTO createOrderDTO;

    @BeforeEach
    void setUp() {
        createOrderDTO = new CreateOrderDTO();
    }

    @Test
    void testSetAndGetUserId() {
        createOrderDTO.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertEquals(42, createOrderDTO.getUserId());
    }

    @Test
    void testSetAndGetAddressId() {
        createOrderDTO.setAddressId(7);
        assertEquals(7, createOrderDTO.getAddressId());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(createOrderDTO.getUserId());
        assertNull(createOrderDTO.getAddressId());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateOrderDTO dto1 = new CreateOrderDTO();
        dto1.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto1.setAddressId(10);

        CreateOrderDTO dto2 = new CreateOrderDTO();
        dto2.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto2.setAddressId(10);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenFieldsDiffer() {
        CreateOrderDTO dto1 = new CreateOrderDTO();
        dto1.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        CreateOrderDTO dto2 = new CreateOrderDTO();
        dto2.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        createOrderDTO.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        createOrderDTO.setAddressId(3);
        String result = createOrderDTO.toString();
        assertTrue(result.contains("5"));
        assertTrue(result.contains("3"));
    }
}