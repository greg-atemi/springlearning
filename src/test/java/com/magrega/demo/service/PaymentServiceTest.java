package com.magrega.demo.service;

import com.magrega.demo.dto.payment.ApprovePaymentDTO;
import com.magrega.demo.dto.payment.CreatePaymentDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Payment;
import com.magrega.demo.model.Product;
import com.magrega.demo.model.enums.PaymentMethod;
import com.magrega.demo.model.enums.PaymentStatus;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.PaymentRepo;
import com.magrega.demo.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private PaymentService paymentService;

    private Order mockOrder;
    private Payment mockPayment;
    private Product mockProduct;
    private OrderItem mockOrderItem;
    private CreatePaymentDTO mockCreateDTO;
    private ApprovePaymentDTO mockApproveDTO;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Galaxy S24");
        mockProduct.setPrice(new BigDecimal("999.99"));
        mockProduct.setQuantity(10);

        mockOrderItem = new OrderItem();
        mockOrderItem.setId(1);
        mockOrderItem.setProduct(mockProduct);
        mockOrderItem.setQuantity(2);
        mockOrderItem.setUnitPrice(new BigDecimal("999.99"));
        mockOrderItem.setSubTotal(new BigDecimal("1999.98"));

        mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setItems(new ArrayList<>(List.of(mockOrderItem)));
        mockOrder.setTotalAmount(new BigDecimal("1999.98"));
        mockOrder.setPaymentStatus(PaymentStatus.PENDING);

        mockPayment = new Payment();
        mockPayment.setId(1);
        mockPayment.setOrder(mockOrder);
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

    // ─────────────────────────────────────────────
    // getAllPayments()
    // ─────────────────────────────────────────────

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        when(paymentRepo.findAll()).thenReturn(List.of(mockPayment));

        List<Payment> result = paymentService.getAllPayments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTransactionReference()).isEqualTo("TXN-001");
        verify(paymentRepo, times(1)).findAll();
    }

    @Test
    void getAllPayments_ShouldReturnEmptyList_WhenNoPayments() {
        when(paymentRepo.findAll()).thenReturn(List.of());

        List<Payment> result = paymentService.getAllPayments();

        assertThat(result).isEmpty();
        verify(paymentRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getPaymentById()
    // ─────────────────────────────────────────────

    @Test
    void getPaymentById_ShouldReturnPayment_WhenExists() {
        when(paymentRepo.findById(1)).thenReturn(Optional.of(mockPayment));

        Payment result = paymentService.getPaymentById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(paymentRepo, times(1)).findById(1);
    }

    @Test
    void getPaymentById_ShouldReturnNull_WhenNotFound() {
        when(paymentRepo.findById(99)).thenReturn(Optional.empty());

        Payment result = paymentService.getPaymentById(99);

        assertThat(result).isNull();
        verify(paymentRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // createPayment()
    // ─────────────────────────────────────────────

    @Test
    void createPayment_ShouldCreateAndReturnPayment_WhenAmountMatches() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.createPayment(mockCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo("1999.98");
        assertThat(result.getTransactionReference()).isEqualTo("TXN-001");
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.MPESA);
        verify(paymentRepo, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_ShouldLinkPaymentToOrder() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.createPayment(mockCreateDTO);

        assertThat(result.getOrder()).isEqualTo(mockOrder);
    }

    @Test
    void createPayment_ShouldSetPaidAt() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.createPayment(mockCreateDTO);

        assertThat(result.getPaidAt()).isNotNull();
    }

    @Test
    void createPayment_ShouldThrow_WhenAmountDoesNotMatchOrderTotal() {
        mockCreateDTO.setAmount(new BigDecimal("500.00")); // wrong amount
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> paymentService.createPayment(mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment amount does not match order total");

        verify(paymentRepo, never()).save(any());
    }

    @Test
    void createPayment_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());
        mockCreateDTO.setOrderId(99);

        assertThatThrownBy(() -> paymentService.createPayment(mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");

        verify(paymentRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // reduceInventory()
    // ─────────────────────────────────────────────

    @Test
    void reduceInventory_ShouldReduceProductStock_ForEachItem() {
        paymentService.reduceInventory(mockOrder);

        // product had 10, order item has qty 2 → should now be 8
        assertThat(mockProduct.getQuantity()).isEqualTo(8);
        verify(productRepo, times(1)).save(mockProduct);
    }

    @Test
    void reduceInventory_ShouldThrow_WhenStockIsInsufficient() {
        mockProduct.setQuantity(1);   // only 1 in stock
        mockOrderItem.setQuantity(5); // but order wants 5

        assertThatThrownBy(() -> paymentService.reduceInventory(mockOrder))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Not enough stock for product: Galaxy S24");

        verify(productRepo, never()).save(any());
    }

    @Test
    void reduceInventory_ShouldReduceStock_ForMultipleItems() {
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("iPad Pro");
        product2.setQuantity(5);

        OrderItem item2 = new OrderItem();
        item2.setProduct(product2);
        item2.setQuantity(3);

        mockOrder.getItems().add(item2);

        paymentService.reduceInventory(mockOrder);

        assertThat(mockProduct.getQuantity()).isEqualTo(8); // 10 - 2
        assertThat(product2.getQuantity()).isEqualTo(2);    // 5 - 3
        verify(productRepo, times(2)).save(any(Product.class));
    }

    // ─────────────────────────────────────────────
    // approvePaymentAndReduceStock()
    // ─────────────────────────────────────────────

    @Test
    void approvePayment_ShouldApproveAndReduceStock_WhenValid() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.findByOrder(mockOrder)).thenReturn(Optional.of(mockPayment));
        when(paymentRepo.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.approvePaymentAndReduceStock(mockApproveDTO);

        assertThat(result).isNotNull();
        assertThat(mockOrder.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(mockProduct.getQuantity()).isEqualTo(8); // 10 - 2
        verify(paymentRepo, times(1)).save(mockPayment);
    }

    @Test
    void approvePayment_ShouldSetPaidAt_OnApproval() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.findByOrder(mockOrder)).thenReturn(Optional.of(mockPayment));
        when(paymentRepo.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.approvePaymentAndReduceStock(mockApproveDTO);

        assertThat(result.getPaidAt()).isNotNull();
    }

    @Test
    void approvePayment_ShouldThrow_WhenAlreadyPaid() {
        mockOrder.setPaymentStatus(PaymentStatus.PAID); // already approved
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.findByOrder(mockOrder)).thenReturn(Optional.of(mockPayment));

        assertThatThrownBy(() -> paymentService.approvePaymentAndReduceStock(mockApproveDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment already approved");

        verify(paymentRepo, never()).save(any());
        verify(productRepo, never()).save(any());
    }

    @Test
    void approvePayment_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());
        mockApproveDTO.setOrderId(99);

        assertThatThrownBy(() -> paymentService.approvePaymentAndReduceStock(mockApproveDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");

        verify(paymentRepo, never()).save(any());
    }

    @Test
    void approvePayment_ShouldThrow_WhenPaymentNotFound() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(paymentRepo.findByOrder(mockOrder)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.approvePaymentAndReduceStock(mockApproveDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment not found");

        verify(paymentRepo, never()).save(any());
        verify(productRepo, never()).save(any());
    }
}