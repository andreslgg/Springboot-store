package com.store.oncommerce_web.repository;

import com.store.oncommerce_web.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(COALESCE(:searchTerm, '') = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "(COALESCE(:category, '') = '' OR p.category = :category) AND " +
            "(COALESCE(:minPrice, 0) <= p.price) AND " +
            "(COALESCE(:maxPrice, 999999) >= p.price) AND " +
            "(COALESCE(:minRating, 0) <= p.rating.rate)")
    List<Product> findProducts(@Param("searchTerm") String searchTerm,
                               @Param("category") String category,
                               @Param("minPrice") Double minPrice,
                               @Param("maxPrice") Double maxPrice,
                               @Param("minRating") Double minRating);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
    List<String> findDistinctCategories();

    @Query("SELECT p FROM Product p WHERE p.discount = (SELECT MAX(p2.discount) FROM Product p2)")
    Product findProductWithBestDiscount();

    @Query("SELECT p FROM Product p")
    List<Product> findAllProducts();
}
