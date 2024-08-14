package io.igorv404.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class MemberHasBorrowedBooksException extends RuntimeException {
  private final String message = "Member has borrowed books";
  private final Integer httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
}
