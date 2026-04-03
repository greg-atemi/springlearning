package com.magrega.demo.service;

import com.magrega.demo.dto.order.CreateOrderDTO;
import com.magrega.demo.dto.order.UpdateOrderStatusDTO;
import com.magrega.demo.model.Address;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.User;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentStatus;
import com.magrega.demo.repository.AddressRepo;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.UserRepo;
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
class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private OrderService orderService;

    private User mockUser;
    private Address mockAddress;
    private Order mockOrder;
    private CreateOrderDTO mockCreateDTO;
    private UpdateOrderStatusDTO mockUpdateStatusDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("Greg");
        mockUser.setLastName("Atemi");
        mockUser.setEmail("greg@systechafrica.com");
        mockUser.setAddressList(new ArrayList<>());

        mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Nairobi");
        mockAddress.setLocality("Westlands");
        mockAddress.setUser(mockUser);

        mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setUser(mockUser);
        mockOrder.setAddress(mockAddress);
        mockOrder.setTotalAmount(BigDecimal.ZERO);
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockOrder.setPaymentStatus(PaymentStatus.PENDING);
        mockOrder.setItems(new ArrayList<>());

        mockCreateDTO = new CreateOrderDTO();
        mockCreateDTO.setUserId(1);
        mockCreateDTO.setAddressId(1);

        mockUpdateStatusDTO = new UpdateOrderStatusDTO();
        mockUpdateStatusDTO.setOrderId(1);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.SHIPPED);
    }

    // ─────────────────────────────────────────────
    // getOrders()
    // ─────────────────────────────────────────────

    @Test
    void getOrders_ShouldReturnAllOrders() {
        when(orderRepo.findAll()).thenReturn(List.of(mockOrder));

        List<Order> result = orderService.getOrders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    void getOrders_ShouldReturnEmptyList_WhenNoOrders() {
        when(orderRepo.findAll()).thenReturn(List.of());

        List<Order> result = orderService.getOrders();

        assertThat(result).isEmpty();
        verify(orderRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getOrderById()
    // ─────────────────────────────────────────────

    @Test
    void getOrderById_ShouldReturnOrder_WhenExists() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        Order result = orderService.getOrderById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(orderRepo, times(1)).findById(1);
    }

    @Test
    void getOrderById_ShouldReturnNull_WhenNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());

        Order result = orderService.getOrderById(99);

        assertThat(result).isNull();
        verify(orderRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // createOrder()
    // ─────────────────────────────────────────────

    @Test
    void createOrder_ShouldCreateAndReturnOrder_WhenValid() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.findById(1)).thenReturn(Optional.of(mockAddress));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(mockCreateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(orderRepo, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldLinkUserAndAddress() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.findById(1)).thenReturn(Optional.of(mockAddress));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(mockCreateDTO);

        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getAddress()).isEqualTo(mockAddress);
    }

    @Test
    void createOrder_ShouldThrow_WhenUserNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());
        mockCreateDTO.setUserId(99);

        assertThatThrownBy(() -> orderService.createOrder(mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(orderRepo, never()).save(any());
        verify(addressRepo, never()).findById(any());
    }

    @Test
    void createOrder_ShouldThrow_WhenAddressNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.findById(99)).thenReturn(Optional.empty());
        mockCreateDTO.setAddressId(99);

        assertThatThrownBy(() -> orderService.createOrder(mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Address not found");

        verify(orderRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // updateOrderById()
    // ─────────────────────────────────────────────

    @Test
    void updateOrderById_ShouldUpdateUser_WhenUserIdProvided() {
        User newUser = new User();
        newUser.setId(2);
        newUser.setFirstName("Jane");
        newUser.setEmail("jane@systechafrica.com");
        newUser.setAddressList(new ArrayList<>());

        mockCreateDTO.setUserId(2);
        mockCreateDTO.setAddressId(null);

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(userRepo.findById(2)).thenReturn(Optional.of(newUser));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderById(1, mockCreateDTO);

        assertThat(result.getUser()).isEqualTo(newUser);
        verify(userRepo, times(1)).findById(2);
    }

    @Test
    void updateOrderById_ShouldUpdateAddress_WhenAddressIdProvided() {
        Address newAddress = new Address();
        newAddress.setId(2);
        newAddress.setLocality("Kilimani");

        mockCreateDTO.setUserId(null);
        mockCreateDTO.setAddressId(2);

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(addressRepo.findById(2)).thenReturn(Optional.of(newAddress));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderById(1, mockCreateDTO);

        assertThat(result.getAddress()).isEqualTo(newAddress);
        verify(addressRepo, times(1)).findById(2);
    }

    @Test
    void updateOrderById_ShouldNotUpdateUserOrAddress_WhenBothNull() {
        mockCreateDTO.setUserId(null);
        mockCreateDTO.setAddressId(null);

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderById(1, mockCreateDTO);

        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getAddress()).isEqualTo(mockAddress);
        verifyNoInteractions(userRepo);
        verifyNoInteractions(addressRepo);
    }

    @Test
    void updateOrderById_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderById(99, mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderById_ShouldThrow_WhenNewUserNotFound() {
        mockCreateDTO.setUserId(99);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderById(1, mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderById_ShouldThrow_WhenNewAddressNotFound() {
        mockCreateDTO.setUserId(null);
        mockCreateDTO.setAddressId(99);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(addressRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderById(1, mockCreateDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Address not found");

        verify(orderRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // deleteOrderById()
    // ─────────────────────────────────────────────

    @Test
    void deleteOrderById_ShouldDelete_WhenOrderExists() {
        when(orderRepo.existsById(1)).thenReturn(true);

        orderService.deleteOrderById(1);

        verify(orderRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteOrderById_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> orderService.deleteOrderById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order with id 99 not found");

        verify(orderRepo, never()).deleteById(any());
    }

    // ─────────────────────────────────────────────
    // updateOrderStatus() — switch cases
    // ─────────────────────────────────────────────

    @Test
    void updateOrderStatus_ShouldSetShipped_WhenCurrentIsPending() {
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.SHIPPED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderStatus(mockUpdateStatusDTO);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    void updateOrderStatus_ShouldSetDelivered_WhenCurrentIsShipped() {
        mockOrder.setOrderStatus(OrderStatus.SHIPPED);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderStatus(mockUpdateStatusDTO);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void updateOrderStatus_ShouldSetCancelled_WhenCurrentIsPending() {
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.CANCELLED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderStatus(mockUpdateStatusDTO);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void updateOrderStatus_ShouldSetPending_WhenCurrentIsShipped() {
        mockOrder.setOrderStatus(OrderStatus.SHIPPED);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.PENDING);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrderStatus(mockUpdateStatusDTO);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void updateOrderStatus_ShouldThrow_WhenAlreadyPending() {
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.PENDING);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> orderService.updateOrderStatus(mockUpdateStatusDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order already Pending");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldThrow_WhenAlreadyShipped() {
        mockOrder.setOrderStatus(OrderStatus.SHIPPED);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.SHIPPED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> orderService.updateOrderStatus(mockUpdateStatusDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order already Shipped");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldThrow_WhenAlreadyDelivered() {
        mockOrder.setOrderStatus(OrderStatus.DELIVERED);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> orderService.updateOrderStatus(mockUpdateStatusDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order already Delivered");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldThrow_WhenAlreadyCancelled() {
        mockOrder.setOrderStatus(OrderStatus.CANCELLED);
        mockUpdateStatusDTO.setOrderStatus(OrderStatus.CANCELLED);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));

        assertThatThrownBy(() -> orderService.updateOrderStatus(mockUpdateStatusDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order already cancelled");

        verify(orderRepo, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());
        mockUpdateStatusDTO.setOrderId(99);

        assertThatThrownBy(() -> orderService.updateOrderStatus(mockUpdateStatusDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");

        verify(orderRepo, never()).save(any());
    }
}
