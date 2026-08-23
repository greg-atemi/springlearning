package com.magrega.demo.service;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.model.Category;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.magrega.demo.model.Product;
import org.springframework.stereotype.Service;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int prodId) {
        return productRepo.findById(prodId).orElse(null);
    }

    // shared by add + update so both apply the exact same validation
    private Set<Category> resolveCategories(Set<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Category> categories = new HashSet<>(categoryRepo.findAllById(categoryIds));
        if (categories.size() != categoryIds.size()) {
            throw new RuntimeException("One or more categories not found");
        }
        return categories;
    }

    public void addProduct(ProductDTO dto) {
        productRepo.save(buildProduct(new Product(), dto));
    }

    public Product addProductAndReturn(ProductDTO dto) {
        return productRepo.save(buildProduct(new Product(), dto));
    }

    private Product buildProduct(Product product, ProductDTO dto) {
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
        product.setCategories(resolveCategories(dto.getCategoryIds()));
        return product;
    }

    public void updateProduct(Product prod) {
        productRepo.save(prod);
    }

    public void deleteProductById(int prodId) {
        productRepo.deleteById(prodId);
    }

    public List<Product> searchProducts(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        String s = (search   != null && !search.isBlank())   ? search   : null;
        String c = (category != null && !category.isBlank()) ? category : null;

        return productRepo.search(s, c, minPrice, maxPrice);
    }

    public Product updateProductById(int id, ProductDTO dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (dto.getBrand()          != null) product.setBrand(dto.getBrand());
        if (dto.getName()           != null) product.setName(dto.getName());
        if (dto.getDescription()    != null) product.setDescription(dto.getDescription());
        if (dto.getPrice()          != null) product.setPrice(dto.getPrice());
        if (dto.getIsAvailable()    != null) product.setIsAvailable(dto.getIsAvailable());
        if (dto.getQuantity()       != null) product.setQuantity(dto.getQuantity());
        if (dto.getImageUrl()       != null) product.setImageUrl(dto.getImageUrl());
        if (dto.getCompareAtPrice() != null) product.setCompareAtPrice(dto.getCompareAtPrice());
        if (dto.getCategoryIds()    != null) product.setCategories(resolveCategories(dto.getCategoryIds()));

        return productRepo.save(product);
    }

    public Product updateStock(int id, int quantity) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(quantity);
        return productRepo.save(product);
    }
}