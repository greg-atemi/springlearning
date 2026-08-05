package com.magrega.demo.service;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.model.Category;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import com.magrega.demo.model.Product;
import org.springframework.stereotype.Service;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Service
public class ProductService
{
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Product> getProducts()
    {
        return productRepo.findAll();
    }

    public Product getProductById(int prodId)
    {
        return productRepo.findById(prodId).orElse(null);
    }

    public void addProduct(ProductDTO dto)
    {
        Product product = new Product();

        product.setBrand(dto.getBrand());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setReleaseDate(dto.getReleaseDate());
        product.setIsAvailable(dto.getIsAvailable());
        product.setQuantity(dto.getQuantity());
        product.setReviewCount(dto.getReviewCount());
        product.setRating(dto.getRating());
        product.setImageUrl(dto.getImageUrl());
        product.setCompareAtPrice(dto.getCompareAtPrice());

        Category category = categoryRepo.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        productRepo.save(product);
    }

    public void updateProduct(Product prod)
    {
        productRepo.save(prod);
    }

    public void deleteProductById(int prodId)
    {
        productRepo.deleteById(prodId);
    }

    public List<Product> searchProducts(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        // Pass null when blank so the query ignores the filter
        String s   = (search   != null && !search.isBlank())   ? search   : null;
        String c   = (category != null && !category.isBlank()) ? category : null;

        return productRepo.search(s, c, minPrice, maxPrice);
    }

    public Product addProductAndReturn(ProductDTO dto) {
        Product product = new Product();
        product.setBrand(dto.getBrand());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setReleaseDate(dto.getReleaseDate());
        product.setIsAvailable(dto.getIsAvailable());
        product.setQuantity(dto.getQuantity());
        product.setReviewCount(dto.getReviewCount());
        product.setRating(dto.getRating());
        product.setImageUrl(dto.getImageUrl());
        product.setCompareAtPrice(dto.getCompareAtPrice());

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        return productRepo.save(product);
    }

    public Product updateProductById(int id, ProductDTO dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (dto.getBrand()        != null) product.setBrand(dto.getBrand());
        if (dto.getName()         != null) product.setName(dto.getName());
        if (dto.getDescription()  != null) product.setDescription(dto.getDescription());
        if (dto.getPrice()        != null) product.setPrice(dto.getPrice());
        if (dto.getIsAvailable()    != null) product.setIsAvailable(dto.getIsAvailable());
        if (dto.getQuantity()     != null) product.setQuantity(dto.getQuantity());
        if (dto.getImageUrl()     != null) product.setImageUrl(dto.getImageUrl());
        if (dto.getCompareAtPrice() != null) product.setCompareAtPrice(dto.getCompareAtPrice());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return productRepo.save(product);
    }

    public Product updateStock(int id, int quantity) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(quantity);
        return productRepo.save(product);
    }
}
