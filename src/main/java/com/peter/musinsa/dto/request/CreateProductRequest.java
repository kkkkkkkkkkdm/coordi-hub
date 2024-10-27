package com.peter.musinsa.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductRequest {
    private String brandName;
    private String categoryName;
    private String name;
    private long price;
}

