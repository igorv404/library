package io.igorv404.library.controller;

import io.igorv404.library.model.RequestError;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(value = {EntityNotFoundException.class})
  private ResponseEntity<RequestError> handleEntityNotFoundException() {
    return new ResponseEntity<>(
        new RequestError("The entity with this id is absent", HttpStatus.NOT_FOUND.value()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  private ResponseEntity<RequestError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    Map<String, List<String>> fieldErrorsMap = new TreeMap<>();
    e.getBindingResult().getFieldErrors().forEach(error -> {
      String fieldName = error.getField();
      String message = error.getDefaultMessage();
      fieldErrorsMap.computeIfAbsent(fieldName, k -> new LinkedList<>()).add(message);
    });
    StringBuilder errorMessage = new StringBuilder();
    fieldErrorsMap.forEach((fieldName, messages) -> {
      String combinedMessages = String.join(", ", messages);
      if (errorMessage.isEmpty()) {
        errorMessage.append(String.format("%s: %s", fieldName, combinedMessages));
      } else {
        errorMessage.append(String.format("; %s: %s", fieldName, combinedMessages));
      }
    });
    return new ResponseEntity<>(
        new RequestError(errorMessage.toString(), HttpStatus.BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }
}
