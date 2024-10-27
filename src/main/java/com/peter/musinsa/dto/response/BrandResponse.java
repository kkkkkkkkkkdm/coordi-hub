package com.peter.musinsa.dto.response;

import com.peter.musinsa.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrandResponse {
  private final Long id;
  private final String name;
  private final boolean enabled;

  public static BrandResponse from(Brand brand) {
    return new BrandResponse(
        brand.getId(),
        brand.getName(),
        brand.isEnabled()
    );
  }
}
