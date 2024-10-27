package com.peter.musinsa.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class CategoryMinMaxPriceResponse {
  private String category;
  private List<BrandPrice> lowestPrice;
  private List<BrandPrice> highestPrice;

  @Getter
  @AllArgsConstructor
  public static class BrandPrice {
    private String brand;
    private Long price;
  }
}