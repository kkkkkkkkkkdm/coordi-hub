package com.peter.musinsa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
  private boolean success;
  private String message;
  private T data;

  public static <T> CommonResponse<T> success(T data) {
    return new CommonResponse<>(true, "Success", data);
  }

  public static <T> CommonResponse<T> error(String message) {
    return new CommonResponse<>(false, message, null);
  }
}