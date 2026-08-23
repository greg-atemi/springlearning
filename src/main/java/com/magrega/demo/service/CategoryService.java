package com.magrega.demo.service;

import com.magrega.demo.dto.category.CategoryDTO;
import com.magrega.demo.model.Category;
import com.magrega.demo.repository.CategoryRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Getter
@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepo.findById(id).orElse(null);
    }

    @Transactional
    public Category createCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setImageUrl(dto.imageUrl());
        return categoryRepo.save(category);
    }

    @Transactional
    public Category updateCategory(int id, CategoryDTO dto) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found."));
        category.setName(dto.name());
        category.setImageUrl(dto.imageUrl());
        return categoryRepo.save(category);
    }

    @Transactional
    public void deleteCategory(int id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        try {
            categoryRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete category: it is still assigned to one or more products.");
        }
    }
}