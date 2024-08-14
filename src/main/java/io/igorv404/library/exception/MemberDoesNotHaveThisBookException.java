package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberDoesNotHaveThisBookException extends RuntimeException {
  private final String message = "Member does not have this book";
  private final Integer httpStatus = HttpStatus.BAD_REQUEST.value();
}
