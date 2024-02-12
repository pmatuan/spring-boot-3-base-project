package org.base.shared.exception;


import org.base.shared.response.AimsCommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice // @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(AimsException.class)
  public ResponseEntity<Object> handleAimsException(AimsException e) {
    e.printStackTrace();

    return ResponseEntity.status(e.getHttpStatus()).body(e.toAimsCommonResponse());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleConversionFailedException(IllegalArgumentException e) {
    e.printStackTrace();

    return new ResponseEntity<>(AimsCommonResponse.badRequest(e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<Object> handleValidationException(BindException e) {
    BindingResult bindingResult = e.getBindingResult();
    StringBuilder errorMessage = new StringBuilder();
    e.printStackTrace();
    for (ObjectError objectError : bindingResult.getAllErrors()) {
      if (objectError instanceof FieldError fieldError) {
        errorMessage.append(fieldError.getField())
            .append(": ")
            .append(fieldError.getDefaultMessage())
            .append("; ");
      } else {
        errorMessage.append(objectError.getDefaultMessage())
            .append("; ");
      }
    }
    return new ResponseEntity<>(
        AimsCommonResponse.badRequest(errorMessage.toString(), ErrorCodeList.InvalidParameter),
        HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUnwantedException(Exception e) {
    e.printStackTrace();

    /**
     * Consider to send this one to the notification channel when an unwanted exception occurs
     */

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AimsCommonResponse.internalError());
  }

}
