package com.magrega.demo.controller;

import com.magrega.demo.model.Product;
import com.magrega.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(service.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id)
    {
        Product product = service.getProductById(id);

        if (product != null)
        {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/product/{id}")
    public void deleteProductById(@PathVariable int id)
    {
        service.deleteProductById(id);
    }

    @PostMapping("/product")
    public void addProduct(@RequestBody Product product)
    {
        service.addProduct(product);
    }

    @PutMapping("/product")
    public void updateProduct(@RequestBody Product product)
    {
        service.updateProduct(product);
    }
}
