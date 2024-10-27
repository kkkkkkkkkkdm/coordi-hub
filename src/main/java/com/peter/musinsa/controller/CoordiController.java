package com.peter.musinsa.controller;

import com.peter.musinsa.dto.response.BrandLowestTotalResponse;
import com.peter.musinsa.dto.response.CategoryLowestPricesResponse;
import com.peter.musinsa.dto.response.CategoryMinMaxPriceResponse;
import com.peter.musinsa.dto.response.CommonResponse;
import com.peter.musinsa.service.CoordiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coordi")
public class CoordiController {

  private final CoordiService coordiService;

  // 1. 카테고리별 최저가격 상품
  @GetMapping("/categories/lowest-prices")
  public ResponseEntity<CommonResponse<CategoryLowestPricesResponse>> getLowestPricesByCategory() {   // CategoryLowestPricesResponse
    return ResponseEntity.ok(CommonResponse.success(coordiService.findLowestPricesByCategory()));
  }

  // 2. 단일 브랜드로 최저가격 코디
  @GetMapping("/brands/lowest-total")
  public ResponseEntity<CommonResponse<BrandLowestTotalResponse>> getLowestTotalPriceBrand() {
    BrandLowestTotalResponse response = coordiService.findLowestTotalPriceBrand();
      return ResponseEntity.ok(CommonResponse.success(response));
  }


  // 3. 특정 카테고리의 고가,저가
  @GetMapping("/categories/prices/min-max")
  public ResponseEntity<CommonResponse<CategoryMinMaxPriceResponse>> getCategoryMinMaxPrices(@RequestParam String categoryName) {
      CategoryMinMaxPriceResponse response = coordiService.findMinMaxPricesByCategory(categoryName);
        return ResponseEntity.ok(CommonResponse.success(response));
  }
}