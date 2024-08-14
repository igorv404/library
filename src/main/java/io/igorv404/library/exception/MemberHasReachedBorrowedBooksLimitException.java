package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberHasReachedBorrowedBooksLimitException extends RuntimeException {
  private final String message = "Member has reached borrowed book limit";
  private final Integer httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
}
