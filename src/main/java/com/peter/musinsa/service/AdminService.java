package com.peter.musinsa.service;


import com.peter.musinsa.domain.Brand;
import com.peter.musinsa.dto.request.CreateBrandRequest;
import com.peter.musinsa.dto.request.UpdateBrandRequest;
import com.peter.musinsa.dto.response.BrandResponse;
import com.peter.musinsa.exception.BusinessException;
import com.peter.musinsa.exception.ErrorCode;
import com.peter.musinsa.repository.BrandRepository;
import com.peter.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final BrandRepository brandRepository;
  private final ProductRepository productRepository;


  // 브랜드 목록 조회
  public Page<BrandResponse> getBrands(PageRequest pageRequest, Boolean enabled) {
    Page<Brand> brands;
    if (enabled != null) {
      brands = brandRepository.findAllByEnabled(enabled, pageRequest);
    } else {
      brands = brandRepository.findAll(pageRequest);
    }

    return brands.map(BrandResponse::from);
  }

  // 브랜드 생성
  @Transactional
  public BrandResponse createBrand(CreateBrandRequest request) {
    if (brandRepository.existsByName(request.getName())) {
      throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 브랜드명입니다.");

    }
    Brand brand = Brand.builder()
        .name(request.getName())
        .enabled(request.isEnabled())
        .build();
    Brand savedBrand = brandRepository.save(brand);
    return BrandResponse.from(savedBrand);
  }



  // 브랜드 수정
  @Transactional
  public BrandResponse updateBrand(Long id, UpdateBrandRequest request) {
    Brand brand = brandRepository.findById(id).orElseThrow();

    if (!brand.getName().equals(request.getName())) {
      if (brandRepository.existsByName(request.getName())) {
        throw new BusinessException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 브랜드명입니다.");
      }
    }
    brand.update(request.getName(), request.isEnabled());
    return BrandResponse.from(brand);
  }

  // 브랜드 삭제
  @Transactional
  public void deleteBrand(Long id) {
    Brand brand = brandRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.BRAND_NOT_FOUND));

    boolean hasProducts = productRepository.existsByBrand(brand);
    boolean is_enabled = brand.isEnabled();
    if (hasProducts | is_enabled) {
      throw new BusinessException(ErrorCode.BRAND_DELETION_NOT_ALLOWED);
    }
    brandRepository.delete(brand);
  }

}

