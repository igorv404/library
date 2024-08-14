package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberHasAlreadyBorrowedThisBookException extends RuntimeException {
  private final String message = "Member has already borrowed this book";
  private final Integer httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
}
