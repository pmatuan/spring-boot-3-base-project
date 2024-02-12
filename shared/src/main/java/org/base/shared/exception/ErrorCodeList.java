package org.base.shared.exception;

public enum ErrorCodeList {
  InternalServerError("500", "Internal Server Error", true),
  InvalidParameter("PARAM_4001", "Invalid Parameter");

  private final String code;
  private final String message;
  private Boolean shouldAlert = false;


  ErrorCodeList(String code, String message) {
    this.code = code;
    this.message = message;
  }

  ErrorCodeList(String code, String message, Boolean shouldAlert) {
    this.code = code;
    this.message = message;
    this.shouldAlert = shouldAlert;
  }

  public String toCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return this.message;
  }

  public Boolean shouldAlert() {
    return shouldAlert;
  }
}
