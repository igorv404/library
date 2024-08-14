package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BookIsBorrowedException extends RuntimeException {
  private final String message = "Book is borrowed";
  private final Integer httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
}
