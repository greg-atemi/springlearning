package com.magrega.demo.dto.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressDTOTest {

    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO();
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
        addressDTO.setLocality("Westlands");
        assertEquals("Westlands", addressDTO.getLocality());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(addressDTO.getMapsPin());
        assertNull(addressDTO.getCounty());
        assertNull(addressDTO.getCountry());
        assertNull(addressDTO.getLocality());
    }

    @Test
    void testEqualsAndHashCode() {
        AddressDTO dto1 = new AddressDTO();
        dto1.setMapsPin("pin1");
        dto1.setCounty("Nairobi");
        dto1.setCountry("Kenya");
        dto1.setLocality("Westlands");

        AddressDTO dto2 = new AddressDTO();
        dto2.setMapsPin("pin1");
        dto2.setCounty("Nairobi");
        dto2.setCountry("Kenya");
        dto2.setLocality("Westlands");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenFieldsDiffer() {
        AddressDTO dto1 = new AddressDTO();
        dto1.setCountry("Kenya");

        AddressDTO dto2 = new AddressDTO();
        dto2.setCountry("Uganda");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        addressDTO.setCountry("Kenya");
        addressDTO.setLocality("Westlands");
        String result = addressDTO.toString();
        assertTrue(result.contains("Kenya"));
        assertTrue(result.contains("Westlands"));
    }
}