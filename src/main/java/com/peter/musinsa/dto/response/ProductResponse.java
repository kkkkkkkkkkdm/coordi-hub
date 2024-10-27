package com.peter.musinsa.dto.response;

import com.peter.musinsa.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private final Long id;
    private final String brandName;
    private final String categoryName;
    private final String name;
    private final Long price;

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getBrand().getName(),
            product.getCategory().getName(),
            product.getName(),
            product.getPrice()
        );
    }
}