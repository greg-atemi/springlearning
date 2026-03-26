package com.magrega.demo.service;

import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Product;
import com.magrega.demo.repository.OrderItemRepo;
import com.magrega.demo.repository.OrderRepo;
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
class OrderItemServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderItemRepo orderItemRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private OrderItemService orderItemService;

    private Order mockOrder;
    private Product mockProduct;
    private OrderItem mockOrderItem;
    private CreateOrderItemDTO mockRequest;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Galaxy S24");
        mockProduct.setPrice(new BigDecimal("999.99"));
        mockProduct.setQuantity(10);  // 10 in stock

        mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setItems(new ArrayList<>());  // mutable list — critical
        mockOrder.setTotalAmount(BigDecimal.ZERO);

        mockOrderItem = new OrderItem();
        mockOrderItem.setId(1);
        mockOrderItem.setProduct(mockProduct);
        mockOrderItem.setQuantity(2);
        mockOrderItem.setUnitPrice(new BigDecimal("999.99"));
        mockOrderItem.setSubTotal(new BigDecimal("1999.98"));

        mockRequest = new CreateOrderItemDTO();
        mockRequest.setOrderId(1);
        mockRequest.setProductId(1);
        mockRequest.setQuantity(2);
    }

    // ─────────────────────────────────────────────
    // getOrderItems()
    // ─────────────────────────────────────────────

    @Test
    void getOrderItems_ShouldReturnAllItems() {
        when(orderItemRepo.findAll()).thenReturn(List.of(mockOrderItem));

        List<OrderItem> result = orderItemService.getOrderItems();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(2);
        verify(orderItemRepo, times(1)).findAll();
    }

    @Test
    void getOrderItems_ShouldReturnEmptyList_WhenNoItems() {
        when(orderItemRepo.findAll()).thenReturn(List.of());

        List<OrderItem> result = orderItemService.getOrderItems();

        assertThat(result).isEmpty();
        verify(orderItemRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getOrderItemById()
    // ─────────────────────────────────────────────

    @Test
    void getOrderItemById_ShouldReturnItem_WhenExists() {
        when(orderItemRepo.findById(1)).thenReturn(Optional.of(mockOrderItem));

        OrderItem result = orderItemService.getOrderItemById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        verify(orderItemRepo, times(1)).findById(1);
    }

    @Test
    void getOrderItemById_ShouldReturnNull_WhenNotFound() {
        when(orderItemRepo.findById(99)).thenReturn(Optional.empty());

        OrderItem result = orderItemService.getOrderItemById(99);

        assertThat(result).isNull();
        verify(orderItemRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // createOrUpdateOrderItem() — NEW ITEM branch
    // ─────────────────────────────────────────────

    @Test
    void createOrUpdate_ShouldCreateNewItem_WhenProductNotInOrder() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(orderItemRepo.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderItem result = orderItemService.createOrUpdateOrderItem(mockRequest);

        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualByComparingTo("999.99");
        assertThat(result.getSubTotal()).isEqualByComparingTo("1999.98");
        verify(orderItemRepo, times(1)).save(any(OrderItem.class));
        verify(orderRepo, times(1)).save(mockOrder);
    }

    @Test
    void createOrUpdate_ShouldLinkProductToNewItem() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(orderItemRepo.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderItem result = orderItemService.createOrUpdateOrderItem(mockRequest);

        assertThat(result.getProduct()).isEqualTo(mockProduct);
    }

    @Test
    void createOrUpdate_ShouldUpdateOrderTotal_WhenNewItemAdded() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(orderItemRepo.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        orderItemService.createOrUpdateOrderItem(mockRequest);

        // order total should be saved — qty 2 * 999.99 = 1999.98
        verify(orderRepo).save(argThat(o ->
                o.getTotalAmount().compareTo(new BigDecimal("1999.98")) == 0
        ));
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenQuantityExceedsStock_NewItem() {
        mockRequest.setQuantity(99);  // only 10 in stock
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Not enough stock available");

        verify(orderItemRepo, never()).save(any());
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenReducingNonExistingItem() {
        mockRequest.setQuantity(-1);  // reducing something not in order
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot reduce non-existing item");

        verify(orderItemRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // createOrUpdateOrderItem() — EXISTING ITEM branch
    // ─────────────────────────────────────────────

    @Test
    void createOrUpdate_ShouldIncreaseQuantity_WhenItemAlreadyInOrder() {
        mockOrder.getItems().add(mockOrderItem);  // product already in order with qty 2
        mockRequest.setQuantity(3);               // add 3 more → total 5

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(orderItemRepo.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderItem result = orderItemService.createOrUpdateOrderItem(mockRequest);

        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getSubTotal()).isEqualByComparingTo(
                new BigDecimal("999.99").multiply(BigDecimal.valueOf(5))
        );
    }

    @Test
    void createOrUpdate_ShouldDecreaseQuantity_WhenNegativeQtyOnExistingItem() {
        mockOrder.getItems().add(mockOrderItem);  // qty 2 in order
        mockRequest.setQuantity(-1);              // reduce by 1 → total 1

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(orderItemRepo.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderItem result = orderItemService.createOrUpdateOrderItem(mockRequest);

        assertThat(result.getQuantity()).isEqualTo(1);
    }

    @Test
    void createOrUpdate_ShouldRemoveItem_WhenQuantityBecomesZero() {
        mockOrder.getItems().add(mockOrderItem);  // qty 2 in order
        mockRequest.setQuantity(-2);              // reduce by 2 → total 0 → remove

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        OrderItem result = orderItemService.createOrUpdateOrderItem(mockRequest);

        assertThat(result).isNull();
        verify(orderItemRepo, times(1)).delete(mockOrderItem);
        verify(orderItemRepo, never()).save(any());
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenNewQtyGoesNegative_ExistingItem() {
        mockOrder.getItems().add(mockOrderItem);  // qty 2 in order
        mockRequest.setQuantity(-5);              // 2 + (-5) = -3 → invalid

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot reduce below zero");

        verify(orderItemRepo, never()).save(any());
        verify(orderItemRepo, never()).delete(any());
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenNewQtyExceedsStock_ExistingItem() {
        mockOrder.getItems().add(mockOrderItem);  // qty 2 in order, stock is 10
        mockRequest.setQuantity(9);               // 2 + 9 = 11 → exceeds stock of 10

        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Not enough stock available");

        verify(orderItemRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // createOrUpdateOrderItem() — Guard checks
    // ─────────────────────────────────────────────

    @Test
    void createOrUpdate_ShouldThrow_WhenQuantityIsZero() {
        mockRequest.setQuantity(0);
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be zero");

        verify(orderItemRepo, never()).save(any());
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenOrderNotFound() {
        when(orderRepo.findById(99)).thenReturn(Optional.empty());
        mockRequest.setOrderId(99);

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order not found");

        verify(orderItemRepo, never()).save(any());
        verify(productRepo, never()).findById(any());
    }

    @Test
    void createOrUpdate_ShouldThrow_WhenProductNotFound() {
        when(orderRepo.findById(1)).thenReturn(Optional.of(mockOrder));
        when(productRepo.findById(99)).thenReturn(Optional.empty());
        mockRequest.setProductId(99);

        assertThatThrownBy(() -> orderItemService.createOrUpdateOrderItem(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found");

        verify(orderItemRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // deleteOrderItemById()
    // ─────────────────────────────────────────────

    @Test
    void deleteOrderItemById_ShouldDelete_WhenItemExists() {
        when(orderItemRepo.existsById(1)).thenReturn(true);

        orderItemService.deleteOrderItemById(1);

        verify(orderItemRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteOrderItemById_ShouldThrow_WhenItemNotFound() {
        when(orderItemRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> orderItemService.deleteOrderItemById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Order item with id 99 not found");

        verify(orderItemRepo, never()).deleteById(any());
    }
}

//This service has the most branching logic of all your services so far, so here's a map of every path covered:
//        ```
//createOrUpdateOrderItem()
//│
//├── Order not found          → RuntimeException("Order not found")
//├── Product not found        → RuntimeException("Product not found")
//├── Quantity == 0            → IllegalArgumentException
//│
//├── NEW ITEM (product not in order)
//│   ├── qty < 0              → RuntimeException("Cannot reduce non-existing item")
//│   ├── qty > stock          → RuntimeException("Not enough stock available")
//│   └── qty valid            → creates item, saves, recalculates total ✅
//│
//└── EXISTING ITEM (product already in order)
//    ├── newQty < 0           → RuntimeException("Cannot reduce below zero")
//    ├── newQty > stock       → RuntimeException("Not enough stock available")
//    ├── newQty == 0          → deletes item, returns null ✅
//        ├── qty increase valid   → updates qty + subtotal ✅
//        └── qty decrease valid   → updates qty + subtotal ✅
