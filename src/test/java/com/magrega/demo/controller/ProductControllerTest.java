package com.magrega.demo.controller;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Product;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.ProductService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

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
    private ProductService productService;

    private Product mockProduct;
    private ProductDTO mockProductDTO;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setBrand("Samsung");
        mockProduct.setName("Galaxy S24");
        mockProduct.setDescription("Flagship smartphone");
        mockProduct.setPrice(new BigDecimal("999.99"));
        mockProduct.setAvailable(true);
        mockProduct.setQuantity(10);

        mockProductDTO = new ProductDTO();
        mockProductDTO.setBrand("Samsung");
        mockProductDTO.setName("Galaxy S24");
        mockProductDTO.setDescription("Flagship smartphone");
        mockProductDTO.setPrice(new BigDecimal("999.99"));
        mockProductDTO.setAvailable(true);
        mockProductDTO.setQuantity(10);
        mockProductDTO.setCategoryId(1);
    }

    @Test
    void GET_products_ShouldReturn200_WithProductList() throws Exception {
        when(productService.getProducts()).thenReturn(List.of(mockProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Galaxy S24"))
                .andExpect(jsonPath("$[0].brand").value("Samsung"));
    }

    @Test
    void GET_products_ShouldReturn200_WithEmptyList() throws Exception {
        when(productService.getProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_productById_ShouldReturn200_WhenFound() throws Exception {
        when(productService.getProductById(1)).thenReturn(mockProduct);

        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Galaxy S24"))
                .andExpect(jsonPath("$.brand").value("Samsung"));
    }

    @Test
    void GET_productById_ShouldReturn404_WhenNotFound() throws Exception {
        when(productService.getProductById(99)).thenReturn(null);

        mockMvc.perform(get("/api/product/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_product_ShouldReturn201_WhenCreated() throws Exception {
        doNothing().when(productService).addProduct(any(ProductDTO.class));

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void PUT_product_ShouldReturn200_WhenUpdated() throws Exception {
        doNothing().when(productService).updateProduct(any(Product.class));

        mockMvc.perform(put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_product_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(productService).deleteProductById(1);

        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isOk());
    }
}