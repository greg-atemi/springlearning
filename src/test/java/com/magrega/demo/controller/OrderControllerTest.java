package com.magrega.demo.controller;

import com.magrega.demo.dto.order.CreateOrderDTO;
import com.magrega.demo.dto.order.UpdateOrderStatusDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentStatus;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private Order mockOrder;
    private CreateOrderDTO mockCreateDTO;
    private UpdateOrderStatusDTO mockUpdateStatusDTO;

    @BeforeEach
    void setUp() {
        mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setTotalAmount(BigDecimal.ZERO);
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockOrder.setPaymentStatus(PaymentStatus.PENDING);
        mockOrder.setItems(new ArrayList<>());

        mockCreateDTO = new CreateOrderDTO();
        mockCreateDTO.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mockCreateDTO.setAddressId(1);

        mockUpdateStatusDTO = new UpdateOrderStatusDTO();
        mockUpdateStatusDTO.setOrderId(1);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.SHIPPED);
    }

    @Test
    void GET_orders_ShouldReturn200_WithOrderList() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of(mockOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderStatus").value("PENDING"));
    }

    @Test
    void GET_orders_ShouldReturn200_WithEmptyList() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_orderById_ShouldReturn200_WhenFound() throws Exception {
        when(orderService.getOrderById(1)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"));
    }

    @Test
    void GET_orderById_ShouldReturn404_WhenNotFound() throws Exception {
        when(orderService.getOrderById(99)).thenReturn(null);

        mockMvc.perform(get("/api/order/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_order_ShouldReturn200_WhenCreated() throws Exception {
        when(orderService.createOrder(any(CreateOrderDTO.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));
    }

    @Test
    void PUT_order_ShouldReturn200_WhenUpdated() throws Exception {
        when(orderService.updateOrderById(eq(1), any(CreateOrderDTO.class)))
                .thenReturn(mockOrder);

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void POST_updateOrderStatus_ShouldReturn200_WhenUpdated() throws Exception {
        mockOrder.setOrderStatus(OrderStatus.SHIPPED);
        when(orderService.updateOrderStatus(any(UpdateOrderStatusDTO.class)))
                .thenReturn(mockOrder);

        mockMvc.perform(post("/api/order/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUpdateStatusDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("SHIPPED"));
    }

    @Test
    void DELETE_order_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(orderService).deleteOrderById(1);

        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());
    }

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

}