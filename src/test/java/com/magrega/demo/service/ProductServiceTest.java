package com.magrega.demo.service;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.model.Category;
import com.magrega.demo.model.Product;
import com.magrega.demo.repository.CategoryRepo;
import com.magrega.demo.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private Category mockCategory;
    private ProductDTO mockProductDTO;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setId(1);
        mockCategory.setName("Electronics");

        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setBrand("Samsung");
        mockProduct.setName("Galaxy S24");
        mockProduct.setDescription("Flagship smartphone");
        mockProduct.setPrice(new BigDecimal("999.99"));
        mockProduct.setReleaseDate(new Date(2024 - 1900, 0, 17));   // Jan 17 2024
        mockProduct.setAvailable(true);
        mockProduct.setQuantity(50);
        mockProduct.setCategory(mockCategory);

        mockProductDTO = new ProductDTO();
        mockProductDTO.setBrand("Samsung");
        mockProductDTO.setName("Galaxy S24");
        mockProductDTO.setDescription("Flagship smartphone");
        mockProductDTO.setPrice(new BigDecimal("999.99"));
        mockProductDTO.setReleaseDate(new Date(2024 - 1900, 0, 17));
        mockProductDTO.setAvailable(true);
        mockProductDTO.setQuantity(50);
        mockProductDTO.setCategoryId(1);
    }

    // ─────────────────────────────────────────────
    // getProducts()
    // ─────────────────────────────────────────────

    @Test
    void getProducts_ShouldReturnAllProducts() {
        // Arrange
        when(productRepo.findAll()).thenReturn(List.of(mockProduct));

        // Act
        List<Product> result = productService.getProducts();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Galaxy S24");
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void getProducts_ShouldReturnEmptyList_WhenNoProducts() {
        when(productRepo.findAll()).thenReturn(List.of());

        List<Product> result = productService.getProducts();

        assertThat(result).isEmpty();
        verify(productRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getProductById()
    // ─────────────────────────────────────────────

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        Product result = productService.getProductById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getBrand()).isEqualTo("Samsung");
        verify(productRepo, times(1)).findById(1);
    }

    @Test
    void getProductById_ShouldReturnNull_WhenNotFound() {
        when(productRepo.findById(99)).thenReturn(Optional.empty());

        Product result = productService.getProductById(99);

        assertThat(result).isNull();
        verify(productRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // addProduct()
    // ─────────────────────────────────────────────

    @Test
    void addProduct_ShouldSaveProduct_WhenCategoryExists() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(mockCategory));

        productService.addProduct(mockProductDTO);

        // Capture what was actually saved and verify its fields
        verify(productRepo, times(1)).save(argThat(savedProduct ->
                savedProduct.getBrand().equals("Samsung") &&
                        savedProduct.getName().equals("Galaxy S24") &&
                        savedProduct.getCategory().getId() == 1 &&
                        savedProduct.isAvailable() &&
                        savedProduct.getQuantity() == 50
        ));
    }

    @Test
    void addProduct_ShouldThrowRuntimeException_WhenCategoryNotFound() {
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());
        mockProductDTO.setCategoryId(99);

        assertThatThrownBy(() -> productService.addProduct(mockProductDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Category not found");

        verify(productRepo, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // updateProduct()
    // ─────────────────────────────────────────────

    @Test
    void updateProduct_ShouldCallSave_WithGivenProduct() {
        productService.updateProduct(mockProduct);

        verify(productRepo, times(1)).save(mockProduct);
    }

    @Test
    void updateProduct_ShouldPersistChangedFields() {
        mockProduct.setPrice(new BigDecimal("799.99"));
        mockProduct.setQuantity(30);

        productService.updateProduct(mockProduct);

        verify(productRepo).save(argThat(p ->
                p.getPrice().compareTo(new BigDecimal("799.99")) == 0 &&
                        p.getQuantity() == 30
        ));
    }

    // ─────────────────────────────────────────────
    // deleteProductById()
    // ─────────────────────────────────────────────

    @Test
    void deleteProductById_ShouldCallDeleteById_WithCorrectId() {
        productService.deleteProductById(1);

        verify(productRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteProductById_ShouldNotInteractWithCategoryRepo() {
        productService.deleteProductById(1);

        verifyNoInteractions(categoryRepo);
    }
}