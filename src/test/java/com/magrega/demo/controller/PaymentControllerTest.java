package com.magrega.demo.controller;

import com.magrega.demo.dto.payment.ApprovePaymentDTO;
import com.magrega.demo.dto.payment.CreatePaymentDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Payment;
import com.magrega.demo.model.enums.PaymentMethod;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.PaymentService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JwtService jwtService;              // ← added

    @MockitoBean
    private UserDetailsService userDetailsService; // ← added

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;        // ← added

    private Payment mockPayment;
    private CreatePaymentDTO mockCreateDTO;
    private ApprovePaymentDTO mockApproveDTO;

    @BeforeEach
    void setUp() {
        mockPayment = new Payment();
        mockPayment.setId(1);
        mockPayment.setAmount(new BigDecimal("1999.98"));
        mockPayment.setPaymentMethod(PaymentMethod.MPESA);
        mockPayment.setTransactionReference("TXN-001");

        mockCreateDTO = new CreatePaymentDTO();
        mockCreateDTO.setOrderId(1);
        mockCreateDTO.setAmount(new BigDecimal("1999.98"));
        mockCreateDTO.setTransactionReference("TXN-001");

        mockApproveDTO = new ApprovePaymentDTO();
        mockApproveDTO.setOrderId(1);
    }

    @Test
    void GET_payments_ShouldReturn200_WithPaymentList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(mockPayment));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionReference").value("TXN-001"));
    }

    @Test
    void GET_payments_ShouldReturn200_WithEmptyList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of());

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_paymentById_ShouldReturn200_WhenFound() throws Exception {
        when(paymentService.getPaymentById(1)).thenReturn(mockPayment);

        mockMvc.perform(get("/api/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionReference").value("TXN-001"))
                .andExpect(jsonPath("$.paymentMethod").value("MPESA"));
    }

    @Test
    void GET_paymentById_ShouldReturn404_WhenNotFound() throws Exception {
        when(paymentService.getPaymentById(99)).thenReturn(null);

        mockMvc.perform(get("/api/payment/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_payment_ShouldReturn200_WhenCreated() throws Exception {
        when(paymentService.createPayment(any(CreatePaymentDTO.class)))
                .thenReturn(mockPayment);

        mockMvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionReference").value("TXN-001"));
    }

    @Test
    void POST_approvePayment_ShouldReturn200_WhenApproved() throws Exception {
        when(paymentService.approvePaymentAndReduceStock(any(ApprovePaymentDTO.class)))
                .thenReturn(mockPayment);

        mockMvc.perform(post("/api/payment/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockApproveDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod").value("MPESA"));
    }
}