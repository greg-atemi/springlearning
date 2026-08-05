package com.magrega.demo.controller;

import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Address;
import com.magrega.demo.service.AddressService;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddressService addressService;

    // Fixed: Injected UserService mock to resolve context initialization failure
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private Address mockAddress;

    @BeforeEach
    void setUp() {
        mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Nairobi");
        mockAddress.setLocalityArea("Westlands");
        mockAddress.setMapsPin("-1.2921,36.8219");
    }

    @Test
    void GET_addresses_ShouldReturn200_WithAddressList() throws Exception {
        when(addressService.getAddresses()).thenReturn(List.of(mockAddress));

        // Fixed path (/api/address) and field name (.localityArea)
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("Kenya"))
                .andExpect(jsonPath("$[0].localityArea").value("Westlands"));
    }

    @Test
    void GET_addresses_ShouldReturn200_WithEmptyList() throws Exception {
        when(addressService.getAddresses()).thenReturn(List.of());

        // Fixed path (/api/address)
        mockMvc.perform(get("/api/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_addressById_ShouldReturn200_WhenFound() throws Exception {
        when(addressService.getAddressById(1)).thenReturn(mockAddress);

        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Kenya"))
                .andExpect(jsonPath("$.county").value("Nairobi"));
    }

    @Test
    void GET_addressById_ShouldReturn404_WhenNotFound() throws Exception {
        when(addressService.getAddressById(99)).thenReturn(null);

        mockMvc.perform(get("/api/address/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_address_ShouldReturn21_WhenAdded() throws Exception {
        // Fixed mock return value and status assertion (201 Created)
        when(addressService.addAddress(any(Address.class))).thenReturn(mockAddress);

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAddress)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country").value("Kenya"));
    }

    @Test
    void PUT_address_ShouldReturn200_WhenUpdated() throws Exception {
        doNothing().when(addressService).updateAddressById(any(Address.class));

        mockMvc.perform(put("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAddress)))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_address_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(addressService).deleteAddressById(1);

        mockMvc.perform(delete("/api/address/1"))
                .andExpect(status().isOk());
    }
}