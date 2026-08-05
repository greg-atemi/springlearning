package com.magrega.demo.controller;

import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.OrderItemService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderItemService orderItemService;

    private OrderItem mockOrderItem;
    private CreateOrderItemDTO mockRequest;

    @BeforeEach
    void setUp() {
        mockOrderItem = new OrderItem();
        mockOrderItem.setId(1);
        mockOrderItem.setQuantity(2);
        mockOrderItem.setUnitPrice(new BigDecimal("999.99"));
        mockOrderItem.setSubTotal(new BigDecimal("1999.98"));

        mockRequest = new CreateOrderItemDTO();
        mockRequest.setOrderId(1);
        mockRequest.setProductId(1);
        mockRequest.setQuantity(2);
    }

    @Test
    void GET_orderItems_ShouldReturn200_WithItemList() throws Exception {
        when(orderItemService.getOrderItems()).thenReturn(List.of(mockOrderItem));

        mockMvc.perform(get("/api/orderItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    void GET_orderItems_ShouldReturn200_WithEmptyList() throws Exception {
        when(orderItemService.getOrderItems()).thenReturn(List.of());

        mockMvc.perform(get("/api/orderItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_orderItemById_ShouldReturn200_WhenFound() throws Exception {
        when(orderItemService.getOrderItemById(1)).thenReturn(mockOrderItem);

        mockMvc.perform(get("/api/orderItem/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void GET_orderItemById_ShouldReturn404_WhenNotFound() throws Exception {
        when(orderItemService.getOrderItemById(99)).thenReturn(null);

        mockMvc.perform(get("/api/orderItem/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_orderItem_ShouldReturn200_WhenCreated() throws Exception {
        when(orderItemService.createOrUpdateOrderItem(any(CreateOrderItemDTO.class)))
                .thenReturn(mockOrderItem);

        mockMvc.perform(post("/api/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void DELETE_orderItem_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(orderItemService).deleteOrderItemById(1);

        mockMvc.perform(delete("/api/orderItem/1"))
                .andExpect(status().isOk());
    }

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

}