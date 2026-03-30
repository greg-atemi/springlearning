package com.magrega.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    private Order order;
    private OrderItem item1;
    private OrderItem item2;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setItems(new ArrayList<>());
        order.setTotalAmount(BigDecimal.ZERO);

        Product product = new Product();
        product.setId(1);
        product.setName("Galaxy S24");
        product.setPrice(new BigDecimal("999.99"));

        item1 = new OrderItem();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setUnitPrice(new BigDecimal("999.99"));
        item1.setSubTotal(new BigDecimal("1999.98"));

        item2 = new OrderItem();
        item2.setId(2);
        item2.setProduct(product);
        item2.setQuantity(1);
        item2.setUnitPrice(new BigDecimal("999.99"));
        item2.setSubTotal(new BigDecimal("999.99"));
    }

    // ─────────────────────────────────────────────
    // addItem()
    // ─────────────────────────────────────────────

    @Test
    void addItem_ShouldAddItemToOrder() {
        order.addItem(item1);

        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems()).contains(item1);
    }

    @Test
    void addItem_ShouldLinkOrderToItem() {
        order.addItem(item1);

        // bidirectional link — item should point back to order
        assertThat(item1.getOrder()).isEqualTo(order);
    }

    @Test
    void addItem_ShouldSupportMultipleItems() {
        order.addItem(item1);
        order.addItem(item2);

        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getItems()).containsExactly(item1, item2);
    }

    // ─────────────────────────────────────────────
    // removeItem()
    // ─────────────────────────────────────────────

    @Test
    void removeItem_ShouldRemoveItemFromOrder() {
        order.addItem(item1);
        order.addItem(item2);

        order.removeItem(item1);

        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems()).doesNotContain(item1);
    }

    @Test
    void removeItem_ShouldClearOrderReferenceOnItem() {
        order.addItem(item1);

        order.removeItem(item1);

        // back-reference should be cleared
        assertThat(item1.getOrder()).isNull();
    }

    @Test
    void removeItem_ShouldLeaveOtherItemsIntact() {
        order.addItem(item1);
        order.addItem(item2);

        order.removeItem(item1);

        assertThat(order.getItems()).containsExactly(item2);
        assertThat(item2.getOrder()).isEqualTo(order);
    }

    @Test
    void removeItem_ShouldDoNothing_WhenItemNotInOrder() {
        order.addItem(item1);

        // item2 was never added
        order.removeItem(item2);

        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getItems()).contains(item1);
    }
}