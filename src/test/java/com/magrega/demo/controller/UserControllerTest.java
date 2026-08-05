package com.magrega.demo.controller;

import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.User;
import com.magrega.demo.model.enums.Role;
import com.magrega.demo.service.AddressService;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AddressService addressService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private User mockUser;

    private final UUID USER_ID =
            UUID.fromString("00000000-0000-0000-0000-000000000001");


    @BeforeEach
    void setUp() {

        mockUser = new User();

        mockUser.setId(USER_ID);
        mockUser.setRole(Role.USER);
        mockUser.setFirstName("Greg");
        mockUser.setLastName("Atemi");
        mockUser.setEmail("greg@systechafrica.com");
        mockUser.setAddressList(new ArrayList<>());
    }


    // =====================================================
    // GET /api/users
    // =====================================================

    @Test
    void GET_users_ShouldReturn200_WithUserList() throws Exception {

        when(userService.getUsers())
                .thenReturn(List.of(mockUser));


        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName")
                        .value("Greg"))
                .andExpect(jsonPath("$[0].email")
                        .value("greg@systechafrica.com"));


        verify(userService)
                .getUsers();
    }


    @Test
    void GET_users_ShouldReturn200_WithEmptyList() throws Exception {

        when(userService.getUsers())
                .thenReturn(List.of());


        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .isEmpty());


        verify(userService)
                .getUsers();
    }



    // =====================================================
    // GET /api/user/{id}
    // =====================================================

    @Test
    void GET_userById_ShouldReturn200_WhenFound() throws Exception {

        when(userService.getUserById(USER_ID))
                .thenReturn(mockUser);


        mockMvc.perform(get("/api/user/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName")
                        .value("Greg"))
                .andExpect(jsonPath("$.email")
                        .value("greg@systechafrica.com"));


        verify(userService)
                .getUserById(USER_ID);
    }



    @Test
    void GET_userById_ShouldReturn404_WhenNotFound() throws Exception {

        when(userService.getUserById(USER_ID))
                .thenReturn(null);


        mockMvc.perform(get("/api/user/" + USER_ID))
                .andExpect(status().isNotFound());


        verify(userService)
                .getUserById(USER_ID);
    }



    // =====================================================
    // PUT /api/user
    // =====================================================

    @Test
    void PUT_user_ShouldReturn200_WhenUpdated() throws Exception {


        doNothing()
                .when(userService)
                .updateUserById(any(User.class));


        mockMvc.perform(put("/api/user")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isOk());


        verify(userService)
                .updateUserById(any(User.class));
    }



    // =====================================================
    // DELETE /api/user/{id}
    // =====================================================

    @Test
    void DELETE_user_ShouldReturn200_WhenDeleted() throws Exception {


        doNothing()
                .when(userService)
                .deleteUserById(USER_ID);


        mockMvc.perform(delete("/api/user/" + USER_ID))
                .andExpect(status().isOk());


        verify(userService)
                .deleteUserById(USER_ID);
    }
}