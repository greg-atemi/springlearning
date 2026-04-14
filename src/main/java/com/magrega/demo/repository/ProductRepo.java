package com.magrega.demo.repository;

import com.magrega.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("""
        SELECT p FROM Product p
        WHERE
          (:search   IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
                             OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:category IS NULL OR p.category.name = :category)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    """)
    List<Product> search(
            @Param("search")   String search,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
}