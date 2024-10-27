package com.peter.musinsa.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CategoryLowestPricesResponse {
  private List<CategoryPrice> categories;
  private Long totalPrice;

@Getter
@AllArgsConstructor
public static class CategoryPrice {
    private String category;
    private String brand;
    private Long price;
  }
}