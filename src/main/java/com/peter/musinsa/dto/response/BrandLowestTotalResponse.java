package com.peter.musinsa.dto.response;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BrandLowestTotalResponse {
  private String brand;
  private List<CategoryPrice> categories;
  private Long totalPrice;

  @Getter
  @AllArgsConstructor
  public static class CategoryPrice {
    private String category;
    private Long price;
  }
}