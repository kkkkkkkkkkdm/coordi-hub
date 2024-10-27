package com.peter.musinsa.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBrandRequest {
  private String name;
  private boolean enabled;
}
