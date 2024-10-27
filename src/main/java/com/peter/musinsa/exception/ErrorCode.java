package com.peter.musinsa.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "COMMON_001", "이미 존재하는 리소스입니다."),

  BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "BRAND_001", "브랜드를 찾을 수 없습니다."),
  BRAND_DELETION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "BRAND_002", "브랜드 삭제가 불가능합니다."),
  NO_BRAND_WITH_CATEGORIES(HttpStatus.UNPROCESSABLE_ENTITY, "BRAND_003", "모든 카테고리의 상품을 보유한 브랜드가 없습니다."),


  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_001", "카테고리를 찾을 수 없습니다."),

  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_001", "상품을 찾을 수 없습니다.");






  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}