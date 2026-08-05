package com.magrega.demo.controller;

import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Category;
import com.magrega.demo.service.CategoryService;
import com.magrega.demo.service.JwtService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setId(1);
        mockCategory.setName("Electronics");
    }

    @Test
    void GET_categories_ShouldReturn200_WithCategoryList() throws Exception {
        when(categoryService.getCategories()).thenReturn(List.of(mockCategory));

        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void GET_categories_ShouldReturn200_WithEmptyList() throws Exception {
        when(categoryService.getCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_categoryById_ShouldReturn200_WhenFound() throws Exception {
        when(categoryService.getCategoryById(1)).thenReturn(mockCategory);

        mockMvc.perform(get("/api/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void GET_categoryById_ShouldReturn404_WhenNotFound() throws Exception {
        when(categoryService.getCategoryById(99)).thenReturn(null);

        mockMvc.perform(get("/api/category/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_category_ShouldReturn200_WhenAdded() throws Exception {
        doNothing().when(categoryService).addCategory(any(Category.class));

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isOk());
    }

    @Test
    void PUT_category_ShouldReturn200_WhenUpdated() throws Exception {
        doNothing().when(categoryService).updateCategoryById(any(Category.class));

        mockMvc.perform(put("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isOk());
    }

    @Test
    void DELETE_category_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(categoryService).deleteCategoryById(1);

        mockMvc.perform(delete("/api/category/1"))
                .andExpect(status().isOk());
    }

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;
}