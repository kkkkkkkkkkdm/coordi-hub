package com.peter.musinsa.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateBrandRequest {
  private String name;
  private boolean enabled = false;
}