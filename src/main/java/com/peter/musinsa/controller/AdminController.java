package com.peter.musinsa.controller;

import com.peter.musinsa.domain.Brand;
import com.peter.musinsa.dto.request.CreateBrandRequest;
import com.peter.musinsa.dto.request.UpdateBrandRequest;
import com.peter.musinsa.dto.response.BrandResponse;
import com.peter.musinsa.dto.response.CommonResponse;
import com.peter.musinsa.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")  // coordi와 동일한 버전 패턴 유지
public class AdminController {

  private final AdminService adminService;

  /**
   * 브랜드 목록 조회
   * 페이징 (기본 5개)
   * ID 역순 정렬
   * enabled 필터링 옵션
   */
  @GetMapping("/brands")
  public ResponseEntity<CommonResponse<Page<BrandResponse>>> getBrands(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(required = false) Boolean enabled) {

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    Page<BrandResponse> brands = adminService.getBrands(pageRequest, enabled);
    return ResponseEntity.ok(CommonResponse.success(brands));
  }


  /**
   * 브랜드 등록
   */
  @PostMapping("/brands")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<CommonResponse<BrandResponse>> createBrand(
      @RequestBody CreateBrandRequest request) {
    BrandResponse brand = adminService.createBrand(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CommonResponse.success(brand));
  }

  /**
   * 브랜드 수정
   */
  @PutMapping("/brands/{id}")
  public ResponseEntity<CommonResponse<BrandResponse>> updateBrand(
      @PathVariable Long id,
      @RequestBody UpdateBrandRequest request) {
    BrandResponse brand = adminService.updateBrand(id, request);
    return ResponseEntity.ok(CommonResponse.success(brand));
  }

  /**
   * 브랜드 삭제
   */
  @DeleteMapping("/brands/{id}")
  public ResponseEntity<CommonResponse<Void>> deleteBrand(@PathVariable Long id) {
    adminService.deleteBrand(id);
    return ResponseEntity.ok(CommonResponse.success(null));
  }
}