package com.magrega.demo.service;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.model.Category;
import lombok.Getter;
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
        product.setAvailable(dto.isAvailable());
        product.setQuantity(dto.getQuantity());

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
}
