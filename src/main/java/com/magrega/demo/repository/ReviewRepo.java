package com.magrega.demo.repository;

import com.magrega.demo.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepo extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    List<Review> findByProductIdOrderByCreatedAtDesc(Integer productId);

    boolean existsByUserIdAndProductId(UUID userId, Integer productId);

    @Query("""
           select count(r) as total, coalesce(avg(r.rating), 0.0) as average
           from Review r where r.product.id = :productId
           """)
    ReviewStats getStats(@Param("productId") Integer productId);

    interface ReviewStats {
        long getTotal();
        double getAverage();
    }
}
