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
  public ErrorResponse handlePlantException(PlantException e) {
    log.error("{} is occured.", e.getErrorCode());

    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }

}
