package com.peter.musinsa.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private String name;

    @Column(nullable = false)
    private long price;

    public Product(Brand brand, Category category, String name, long price ) {
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public void update(Brand brand, Category category, String name, long price) {
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
    }
}
