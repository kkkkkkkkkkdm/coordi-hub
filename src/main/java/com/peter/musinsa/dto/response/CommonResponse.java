package com.peter.musinsa.dto.response;

import com.peter.musinsa.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonResponse<T> {
  private boolean success;
  private T data;
  private Error error;

  public static <T> CommonResponse<T> success(T data) {
    CommonResponse<T> response = new CommonResponse<>();
    response.success = true;
    response.data = data;
    return response;
  }

  public static <T> CommonResponse<T> error(ErrorCode errorCode, String message) {
    CommonResponse<T> response = new CommonResponse<>();
    response.success = false;
    response.error = new Error(errorCode.getCode(), message);
    return response;
  }

  @Getter
  @AllArgsConstructor
  public static class Error {
    private String code;
    private String message;
  }
}