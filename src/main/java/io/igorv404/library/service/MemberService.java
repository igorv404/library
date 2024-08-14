package io.igorv404.library.service;

import io.igorv404.library.dto.response.MemberBorrowedBooksDto;
import io.igorv404.library.exception.BookIsUnavailableNowException;
import io.igorv404.library.exception.MemberDoesNotHaveThisBookException;
import io.igorv404.library.exception.MemberHasAlreadyBorrowedThisBookException;
import io.igorv404.library.exception.MemberHasBorrowedBooksException;
import io.igorv404.library.exception.MemberHasReachedBorrowedBooksLimitException;
import io.igorv404.library.model.Book;
import io.igorv404.library.model.Member;
import io.igorv404.library.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Setter
@RequiredArgsConstructor
public class MemberService {
  private final MemberRepository memberRepository;

  private final BookService bookService;

  @Value("${app.max-amount-of-borrowed-books-per-member}")
  private Integer maxAmountOfBorrowedBooksPerMember;

  public List<Member> findAll() {
    return memberRepository.findAll();
  }

  public Member findById(Integer id) {
    return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Member save(String memberName) {
    return memberRepository.save(new Member(memberName));
  }

  public Member update(Integer id, String memberName) {
    Member existingMember = findById(id);
    existingMember.setName(memberName);
    return memberRepository.save(existingMember);
  }

  public String delete(Integer id) {
    Member existingMember = findById(id);
    if (existingMember.getBorrowedBooks().isEmpty()) {
      memberRepository.deleteById(id);
    } else {
      throw new MemberHasBorrowedBooksException();
    }
    return String.format("Member \"%s\" was deleted", existingMember.getName());
  }

  public String borrowBook(Integer memberId, Integer bookId) {
    Member existingMember = findById(memberId);
    Book existingBook = bookService.findById(bookId);
    if (existingMember.getBorrowedBooks().size() >= maxAmountOfBorrowedBooksPerMember) {
      throw new MemberHasReachedBorrowedBooksLimitException();
    } else if (existingMember.getBorrowedBooks().contains(existingBook)) {
      throw new MemberHasAlreadyBorrowedThisBookException();
    } else if (existingBook.getAmount() <= 0) {
      throw new BookIsUnavailableNowException();
    }
    existingBook.setAmount(existingBook.getAmount() - 1);
    bookService.update(existingBook);
    existingMember.getBorrowedBooks().add(existingBook);
    memberRepository.save(existingMember);
    return "Thanks for borrowing the book";
  }

  public String returnBook(Integer memberId, Integer bookId) {
    Member existingMember = findById(memberId);
    Book existingBook = bookService.findById(bookId);
    if (existingMember.getBorrowedBooks().contains(existingBook)) {
      existingBook.setAmount(existingBook.getAmount() + 1);
      bookService.update(existingBook);
      existingMember.getBorrowedBooks().remove(existingBook);
    } else {
      throw new MemberDoesNotHaveThisBookException();
    }
    memberRepository.save(existingMember);
    return "Thanks for returning the book";
  }

  public List<MemberBorrowedBooksDto> showMemberBorrowedBooks(String name) {
    List<MemberBorrowedBooksDto> memberBorrowedBooksDtoList = new LinkedList<>();
    List<Member> matchedMembers = memberRepository.findAllByName(name);
    matchedMembers.forEach(member -> {
      List<String> titlesOfBorrowedBooks = member.getBorrowedBooks().stream()
          .map(Book::getTitle)
          .toList();
      MemberBorrowedBooksDto memberBorrowedBooksDto = new MemberBorrowedBooksDto(member.getId(),
          member.getName(), titlesOfBorrowedBooks);
      memberBorrowedBooksDtoList.add(memberBorrowedBooksDto);
    });
    return memberBorrowedBooksDtoList;
  }
}
