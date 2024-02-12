package org.base.shared.exception;

import lombok.Getter;
import org.base.shared.response.AimsCommonResponse;
import org.springframework.http.HttpStatus;

@Getter
public class AimsException extends RuntimeException {

  private final String code;

  private final String message;

  private final HttpStatus httpStatus;

  public AimsException(Throwable cause, String code, String message, HttpStatus httpStatus) {
    super(cause);
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public AimsException(Throwable cause, ErrorCodeList errorCode, HttpStatus httpStatus) {
    super(cause);
    this.code = errorCode.toCode();
    this.message = errorCode.toString();
    this.httpStatus = httpStatus;
  }

  public AimsCommonResponse<String> toAimsCommonResponse() {
    return new AimsCommonResponse<>(
        this.code,
        this.message,
        null
    );
  }
}
