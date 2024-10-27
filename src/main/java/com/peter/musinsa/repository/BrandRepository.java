package com.peter.musinsa.repository;

import com.peter.musinsa.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
    boolean existsByName(String name);

    Page<Brand> findAllByEnabled(boolean enabled, Pageable pageable);


}