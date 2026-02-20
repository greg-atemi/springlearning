package com.magrega.demo.service;

import com.magrega.demo.model.Category;
import com.magrega.demo.repository.CategoryRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class CategoryService
{
    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getCategories()
    {
        return categoryRepo.findAll();
    }

    public Category getCategoryById(int id)
    {
        return categoryRepo.findById(id).orElse(null);
    }
//
//    public void addBrand(Category prod)
//    {
//        categoryRepo.save(prod);
//    }
//
//    public void updateBrand(Category prod)
//    {
//        categoryRepo.save(prod);
//    }
//
//    public void deleteBrandById(int prodId)
//    {
//        categoryRepo.deleteById(prodId);
//    }
}
