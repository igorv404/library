package io.igorv404.library.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MemberServiceTest {
  @Mock
  private MemberRepository memberRepository;

  @Mock
  private BookService bookService;

  @InjectMocks
  private MemberService memberService;

  private final Member member1 = new Member("John Doe");

  private final Member member2 = new Member("Tom Ford");

  private AutoCloseable mocks;

  @BeforeEach
  public void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void tearDown() {
    try {
      mocks.close();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void shouldFindAllMembers() {
    when(memberRepository.findAll()).thenReturn(List.of(member1, member2));
    List<Member> responseData = memberService.findAll();
    assertNotNull(responseData);
    assertEquals(2, responseData.size());
    verify(memberRepository, times(1)).findAll();
  }

  @Test
  public void shouldFindMemberById() {
    final Integer ID = 1;
    when(memberRepository.findById(ID)).thenReturn(Optional.of(member1));
    Member responseData = memberService.findById(ID);
    assertNotNull(responseData);
    assertEquals(member1, responseData);
    verify(memberRepository, times(1)).findById(ID);
  }

  @Test
  public void shouldThrowExceptionIfMemberNotFound() {
    final Integer ID = 0;
    when(memberRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> memberService.findById(ID));
    verify(memberRepository, times(1)).findById(ID);
  }

  @Test
  public void shouldCreateMember() {
    when(memberRepository.save(any(Member.class))).thenReturn(member1);
    Member responseData = memberService.save(member1.getName());
    assertNotNull(responseData);
    assertEquals(member1, responseData);
    verify(memberRepository, times(1)).save(any(Member.class));
  }

  @Test
  public void shouldUpdateMember() {
    final Integer ID = 1;
    final Member member = new Member("Johnny");
    when(memberRepository.findById(ID)).thenReturn(Optional.of(member1));
    when(memberRepository.save(member1)).thenReturn(member);
    Member responseData = memberService.update(ID, member.getName());
    assertNotNull(responseData);
    assertEquals(member, responseData);
    verify(memberRepository, times(1)).save(member1);
  }

  @Test
  public void shouldDeleteMember() {
    final Integer ID = 1;
    when(memberRepository.findById(ID)).thenReturn(Optional.of(member1));
    assertDoesNotThrow(() -> memberService.delete(ID));
    verify(memberRepository, times(1)).findById(ID);
    verify(memberRepository, times(1)).deleteById(ID);
  }

  @Test
  public void shouldThrowExceptionIfItTriesToDeleteMemberWithBorrowedBooks() {
    final Integer ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 5);
    when(memberRepository.findById(ID)).thenReturn(Optional.of(member1));
    member1.getBorrowedBooks().add(book);
    assertThrows(MemberHasBorrowedBooksException.class, () -> memberService.delete(ID));
    verify(memberRepository, times(1)).findById(ID);
    verify(memberRepository, times(0)).deleteById(ID);
  }

  @Test
  public void shouldBorrowBook() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 5);
    Integer MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER = 1;
    memberService.setMaxAmountOfBorrowedBooksPerMember(MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book);
    assertDoesNotThrow(() -> memberService.borrowBook(MEMBER_ID, BOOK_ID));
    assertEquals(4, book.getAmount());
    assertTrue(member1.getBorrowedBooks().contains(book));
    verify(bookService, times(1)).update(book);
    verify(memberRepository, times(1)).save(member1);
  }

  @Test
  public void shouldThrowExceptionWhenMemberReachedMaxAmountOfBorrowedBooks() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 2;
    final Book book1 = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 5);
    final Book book2 = new Book(2, "Harry Potter and the Chamber of Secrets",
        "Joanne Rowling", 5);
    Integer MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER = 1;
    memberService.setMaxAmountOfBorrowedBooksPerMember(MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER);
    member1.getBorrowedBooks().add(book1);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book2);
    assertThrows(MemberHasReachedBorrowedBooksLimitException.class, () -> memberService.borrowBook(MEMBER_ID, BOOK_ID));
    verify(bookService, times(0)).update(book2);
    verify(memberRepository, times(0)).save(member1);
  }

  @Test
  public void shouldThrowExceptionWhenMemberHasAlreadyBorrowedThisBook() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 5);
    Integer MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER = 2;
    memberService.setMaxAmountOfBorrowedBooksPerMember(MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER);
    member1.getBorrowedBooks().add(book);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book);
    assertThrows(MemberHasAlreadyBorrowedThisBookException.class, () -> memberService.borrowBook(MEMBER_ID, BOOK_ID));
    verify(bookService, times(0)).update(book);
    verify(memberRepository, times(0)).save(member1);
  }

  @Test
  public void shouldThrowExceptionWhenBookIsUnavailableNow() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 0);
    Integer MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER = 1;
    memberService.setMaxAmountOfBorrowedBooksPerMember(MAX_AMOUNT_OF_BORROWED_BOOKS_PER_MEMBER);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book);
    assertThrows(
        BookIsUnavailableNowException.class, () -> memberService.borrowBook(MEMBER_ID, BOOK_ID));
    verify(bookService, times(0)).update(book);
    verify(memberRepository, times(0)).save(member1);
  }

  @Test
  public void shouldReturnBook() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 0);
    member1.getBorrowedBooks().add(book);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book);
    assertDoesNotThrow(() -> memberService.returnBook(MEMBER_ID, BOOK_ID));
    assertEquals(1, book.getAmount());
    assertEquals(0, member1.getBorrowedBooks().size());
    verify(bookService, times(1)).update(book);
    verify(memberRepository, times(1)).save(member1);
  }

  @Test
  public void shouldThrowExceptionWhenTriesToReturnBookWhichMemberDidNotBorrow() {
    final Integer MEMBER_ID = 1;
    final Integer BOOK_ID = 1;
    final Book book = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 1);
    when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member1));
    when(bookService.findById(BOOK_ID)).thenReturn(book);
    assertThrows(MemberDoesNotHaveThisBookException.class, () -> memberService.returnBook(MEMBER_ID, BOOK_ID));
    verify(bookService, times(0)).update(book);
    verify(memberRepository, times(0)).save(member1);
  }

  @Test
  public void shouldShowMemberBorrowedBooks() {
    final Member member = new Member("John Doe");
    final Book book1 = new Book(1, "Harry Potter and the Philosopher's Stone",
        "Joanne Rowling", 5);
    final Book book2 = new Book(2, "Harry Potter and the Chamber of Secrets",
        "Joanne Rowling", 5);
    member.getBorrowedBooks().addAll(List.of(book1, book2));
    member1.getBorrowedBooks().add(book1);
    when(memberRepository.findAllByName(member.getName())).thenReturn(List.of(member, member1));
    List<MemberBorrowedBooksDto> responseData = memberService.showMemberBorrowedBooks(member.getName());
    assertNotNull(responseData);
    assertEquals(2, responseData.size());
    assertTrue(responseData.getFirst().borrowedBooks().contains("Harry Potter and the Philosopher's Stone"));
    verify(memberRepository, times(1)).findAllByName(member.getName());
  }
}
