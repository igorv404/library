package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BookIsUnavailableNowException extends RuntimeException {
  private final String message = "Book is not available now";
  private final Integer httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
}
