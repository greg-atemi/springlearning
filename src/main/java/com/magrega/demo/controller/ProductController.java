package com.magrega.demo.controller;

import com.magrega.demo.dto.product.ProductDTO;
import com.magrega.demo.model.Product;
import com.magrega.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductController
{
    @Autowired
    ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        if (search != null || category != null || minPrice != null || maxPrice != null) {
            return new ResponseEntity<>(
                    service.searchProducts(search, category, minPrice, maxPrice),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(service.getProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
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

    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable int id)
    {
        service.deleteProductById(id);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO dto)
    {
        service.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/products")
    public void updateProduct(@RequestBody Product product)
    {
        service.updateProduct(product);
    }
}
