package com.magrega.demo.controller;

import com.magrega.demo.dto.address.CreateAddressDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Address;
import com.magrega.demo.model.User;
import com.magrega.demo.service.AddressService;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AddressService addressService;

    private User mockUser;
    private Address mockAddress;
    private CreateAddressDTO mockAddressDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mockUser.setFirstName("Greg");
        mockUser.setLastName("Atemi");
        mockUser.setEmail("greg@systechafrica.com");
        mockUser.setAddressList(new ArrayList<>());

        mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Nairobi");
        mockAddress.setCityTown("Westlands");
        mockAddress.setMapsPin("-1.2921,36.8219");
        mockAddress.setUser(mockUser);

        mockAddressDTO = new CreateAddressDTO();
        mockAddressDTO.setCountry("Kenya");
        mockAddressDTO.setCounty("Nairobi");
        mockAddressDTO.setLocality("Westlands");
        mockAddressDTO.setMapsPin("-1.2921,36.8219");
    }

    @Test
    void GET_users_ShouldReturn200_WithUserList() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Greg"))
                .andExpect(jsonPath("$[0].email").value("greg@systechafrica.com"));
    }

    @Test
    void GET_users_ShouldReturn200_WithEmptyList() throws Exception {
        when(userService.getUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_userById_ShouldReturn200_WhenFound() throws Exception {
        when(userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(mockUser);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Greg"))
                .andExpect(jsonPath("$.email").value("greg@systechafrica.com"));
    }

    @Test
    void GET_userById_ShouldReturn404_WhenNotFound() throws Exception {
        when(userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(null);

        mockMvc.perform(get("/api/user/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_user_ShouldReturn200_WhenAdded() throws Exception {
        doNothing().when(userService).addUser(any(User.class));

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk());
    }

    @Test
    void POST_addAddressToUser_ShouldReturn200_WhenAdded() throws Exception {
        when(addressService.addAddressToUser(eq(UUID.fromString("00000000-0000-0000-0000-000000000001")), any(AddressDTO.class)))
                .thenReturn(mockAddress);

        mockMvc.perform(post("/api/user/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAddressDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Kenya"))
                .andExpect(jsonPath("$.locality").value("Westlands"));
    }

    @Test
    void PUT_user_ShouldReturn200_WhenUpdated() throws Exception {
        doNothing().when(userService).updateUserById(any(User.class));

        mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_user_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(userService).deleteUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
    }
}