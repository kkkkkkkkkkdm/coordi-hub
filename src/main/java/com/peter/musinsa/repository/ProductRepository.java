package com.peter.musinsa.repository;

import com.peter.musinsa.domain.Brand;
import com.peter.musinsa.domain.Category;
import com.peter.musinsa.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p FROM Product p " +
        "WHERE p.category = :category AND p.price = (" +
        "SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = :category) " +
        "ORDER BY p.id DESC")
    List<Product> findAllMinPriceProductsByCategory(@Param("category") Category category);

    default Optional<Product> findMinPriceProductByCategory(Category category) {
        return findAllMinPriceProductsByCategory(category)
            .stream()
            .findFirst();
    }


    @Query(value = "SELECT p FROM Product p " +
        "WHERE p.category = :category AND p.price = (" +
        "SELECT MAX(p2.price) FROM Product p2 WHERE p2.category = :category) " +
        "ORDER BY p.id DESC")
    List<Product> findAllMaxPriceProductsByCategory(@Param("category") Category category);

    default Optional<Product> findMaxPriceProductByCategory(Category category) {
        return findAllMaxPriceProductsByCategory(category)
            .stream()
            .findFirst();
    }



    @Query("SELECT DISTINCT p.category.name, MIN(p.price) " +
        "FROM Product p " +
        "WHERE p.brand = :brand " +
        "GROUP BY p.category.name " +
        "ORDER BY p.category.name")
    List<Object[]> findCategoryMinPricesByBrand(@Param("brand") Brand brand);


    List<Product> findAllByBrand(Brand brand);
    Optional<Product> findByBrandAndCategory(Brand brand, Category category);




    // TODO : 카테고리 목록을 입력으로 받는다.
    @Query("SELECT COUNT(DISTINCT p.category) = :totalCategories " +
        "FROM Product p " +
        "WHERE p.brand = :brand")
    boolean hasAllCategories(@Param("brand") Brand brand, @Param("totalCategories") long totalCategories);
}

