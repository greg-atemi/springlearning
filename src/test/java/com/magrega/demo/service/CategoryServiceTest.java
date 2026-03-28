package com.magrega.demo.service;

import com.magrega.demo.model.Category;
import com.magrega.demo.repository.CategoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setId(1);
        mockCategory.setName("Electronics");
    }

    // ─────────────────────────────────────────────
    // getCategories()
    // ─────────────────────────────────────────────

    @Test
    void getCategories_ShouldReturnAllCategories() {
        when(categoryRepo.findAll()).thenReturn(List.of(mockCategory));

        List<Category> result = categoryService.getCategories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Electronics");
        verify(categoryRepo, times(1)).findAll();
    }

    @Test
    void getCategories_ShouldReturnEmptyList_WhenNoCategories() {
        when(categoryRepo.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.getCategories();

        assertThat(result).isEmpty();
        verify(categoryRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getCategoryById()
    // ─────────────────────────────────────────────

    @Test
    void getCategoryById_ShouldReturnCategory_WhenExists() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(mockCategory));

        Category result = categoryService.getCategoryById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Electronics");
        verify(categoryRepo, times(1)).findById(1);
    }

    @Test
    void getCategoryById_ShouldReturnNull_WhenNotFound() {
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        Category result = categoryService.getCategoryById(99);

        assertThat(result).isNull();
        verify(categoryRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // addCategory()
    // ─────────────────────────────────────────────

    @Test
    void addCategory_ShouldCallSave_WithGivenCategory() {
        categoryService.addCategory(mockCategory);

        verify(categoryRepo, times(1)).save(mockCategory);
    }

    @Test
    void addCategory_ShouldSave_WithCorrectFields() {
        categoryService.addCategory(mockCategory);

        verify(categoryRepo).save(argThat(c ->
                c.getName().equals("Electronics") &&
                        c.getId() == 1
        ));
    }

    // ─────────────────────────────────────────────
    // updateCategoryById()
    // ─────────────────────────────────────────────

    @Test
    void updateCategoryById_ShouldCallSave_WithGivenCategory() {
        categoryService.updateCategoryById(mockCategory);

        verify(categoryRepo, times(1)).save(mockCategory);
    }

    @Test
    void updateCategoryById_ShouldPersistChangedFields() {
        mockCategory.setName("Home & Kitchen");

        categoryService.updateCategoryById(mockCategory);

        verify(categoryRepo).save(argThat(c ->
                c.getName().equals("Home & Kitchen")
        ));
    }

    // ─────────────────────────────────────────────
    // deleteCategoryById()
    // ─────────────────────────────────────────────

    @Test
    void deleteCategoryById_ShouldCallDeleteById_WithCorrectId() {
        categoryService.deleteCategoryById(1);

        verify(categoryRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteCategoryById_ShouldNotCallFindOrSave() {
        categoryService.deleteCategoryById(1);

        verify(categoryRepo, never()).findById(any());
        verify(categoryRepo, never()).save(any());
    }
}