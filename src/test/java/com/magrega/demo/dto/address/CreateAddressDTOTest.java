package com.magrega.demo.dto.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAddressDTOTest {

    private CreateAddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new CreateAddressDTO();
    }

    @Test
    void testSetAndGetMapsPin() {
        addressDTO.setMapsPin("https://maps.pin/abc123");
        assertEquals("https://maps.pin/abc123", addressDTO.getMapsPin());
    }

    @Test
    void testSetAndGetCounty() {
        addressDTO.setCounty("Nairobi");
        assertEquals("Nairobi", addressDTO.getCounty());
    }

    @Test
    void testSetAndGetCountry() {
        addressDTO.setCountry("Kenya");
        assertEquals("Kenya", addressDTO.getCountry());
    }

    @Test
    void testSetAndGetLocality() {
        addressDTO.setLocalityArea("Westlands");
        assertEquals("Westlands", addressDTO.getLocalityArea());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(addressDTO.getMapsPin());
        assertNull(addressDTO.getCounty());
        assertNull(addressDTO.getCountry());
        assertNull(addressDTO.getLocalityArea());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateAddressDTO dto1 = new CreateAddressDTO();
        dto1.setMapsPin("pin1");
        dto1.setCounty("Nairobi");
        dto1.setCountry("Kenya");
        dto1.setLocalityArea("Westlands");

        CreateAddressDTO dto2 = new CreateAddressDTO();
        dto2.setMapsPin("pin1");
        dto2.setCounty("Nairobi");
        dto2.setCountry("Kenya");
        dto2.setLocalityArea("Westlands");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenFieldsDiffer() {
        CreateAddressDTO dto1 = new CreateAddressDTO();
        dto1.setCountry("Kenya");

        CreateAddressDTO dto2 = new CreateAddressDTO();
        dto2.setCountry("Uganda");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        addressDTO.setCountry("Kenya");
        addressDTO.setLocalityArea("Westlands");
        String result = addressDTO.toString();
        assertTrue(result.contains("Kenya"));
        assertTrue(result.contains("Westlands"));
    }
}