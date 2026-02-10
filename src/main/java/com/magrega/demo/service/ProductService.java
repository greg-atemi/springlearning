package com.magrega.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import com.magrega.demo.model.Product;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ProductService
{
    List<Product> products = new ArrayList<>(Arrays.asList(
            new Product(101, "Samsung", 10000),
            new Product(102, "Sony", 20000)));

    public Product getProductById(int prodId)
    {
        return products.stream()
            .filter(p -> p.getProdId() == prodId)
            .findFirst().orElse(new Product(0, "No Item", 0));
    }

    public void addProduct(Product prod)
    {
        products.add(prod);
    }

    public void updateProduct(Product prod) {
        int index = 0;

        for (int i = 0; i < products.size(); i++) {
            if  (products.get(i).getProdId() == prod.getProdId()) {
                index = i;
            }
        }

        System.out.println("Index " + index);
        System.out.println("Updating Product " + prod);

        products.set(index, prod);
    }
}
