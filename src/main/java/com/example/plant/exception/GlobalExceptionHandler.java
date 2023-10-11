package com.example.plant.exception;

import static com.example.plant.type.ErrorCode.INVALID_REQUEST;
import static com.example.plant.type.ErrorCode.INVALID_SERVER_ERROR;

import com.example.plant.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PlantException.class)
  public ErrorResponse handleAccountException(PlantException e) {
    log.error("{} is occured.", e.getErrorCode());

    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occurred.", e);

    return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException is occurred.", e);

    return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(PlantException e) {
    log.error("Exception is occured.", e);

    return new ErrorResponse(
        INVALID_SERVER_ERROR,
        INVALID_SERVER_ERROR.getDescription());
  }
}
