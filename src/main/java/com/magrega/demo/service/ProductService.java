package com.magrega.demo.service;

import lombok.Getter;
import java.util.List;
import com.magrega.demo.model.Product;
import org.springframework.stereotype.Service;
import com.magrega.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Service
public class ProductService
{
    @Autowired
    private ProductRepo productRepo;

    public List<Product> getProducts()
    {
        return productRepo.findAll();
    }

    public Product getProductById(int prodId)
    {
        return productRepo.findById(prodId).orElse(null);
    }

    public void addProduct(Product prod)
    {
        productRepo.save(prod);
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
