package io.igorv404.library.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.igorv404.library.dto.response.BorrowedBookDto;
import io.igorv404.library.exception.BookIsBorrowedException;
import io.igorv404.library.model.Book;
import io.igorv404.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BookServiceTest {
  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  private final Book book1 = new Book(1, "Harry Potter and the Philosopher's Stone",
      "Joanne Rowling", 5);

  private final Book book2 = new Book(2, "Harry Potter and the Chamber of Secrets",
      "Joanne Rowling", 5);

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
  public void shouldReturnAllBooks() {
    when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
    List<Book> responseData = bookService.findAll();
    assertNotNull(responseData);
    assertEquals(2, responseData.size());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  public void shouldFindBookById() {
    final Integer ID = 1;
    when(bookRepository.findById(ID)).thenReturn(Optional.of(book1));
    Book responseData = bookService.findById(ID);
    assertNotNull(responseData);
    assertEquals(book1, responseData);
    verify(bookRepository, times(1)).findById(ID);
  }

  @Test
  public void shouldThrowExceptionIfBookNotFound() {
    final Integer ID = 0;
    when(bookRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> bookService.findById(ID));
    verify(bookRepository, times(1)).findById(ID);
  }

  @Test
  public void shouldCreateBook() {
    when(bookRepository.save(book1)).thenReturn(book1);
    Book responseData = bookService.create(book1);
    assertNotNull(responseData);
    assertEquals(book1, responseData);
    verify(bookRepository, times(1)).save(book1);
  }

  @Test
  public void shouldIncreaseBookAmountIfItAlreadyExists() {
    Book book = new Book(1, "Harry Potter and the Philosopher's Stone", "Joanne Rowling", 6);
    when(bookRepository.save(book1)).thenReturn(book);
    Book responseData = bookService.create(book1);
    assertNotNull(responseData);
    assertEquals(book, responseData);
    verify(bookRepository, times(1)).save(book1);
  }

  @Test
  public void shouldUpdateBook() {
    Book book = new Book(1, "Harry Potter and the Prisoner of Azkaban", "Joanne Rowling", 10);
    when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book1));
    when(bookRepository.save(book1)).thenReturn(book);
    Book responseData = bookService.update(book);
    System.out.println(responseData.getTitle());
    assertNotNull(responseData);
    assertEquals(book, responseData);
    verify(bookRepository, times(1)).findById(book.getId());
  }

  @Test
  public void shouldDeleteBook() {
    final Integer ID = 1;
    when(bookRepository.findById(ID)).thenReturn(Optional.of(book1));
    when(bookRepository.isBookBorrowed(ID)).thenReturn(false);
    assertDoesNotThrow(() -> bookService.delete(ID));
    verify(bookRepository, times(1)).findById(ID);
    verify(bookRepository, times(1)).deleteById(ID);
  }

  @Test
  public void shouldThrowExceptionWhenTriesToDeleteBorrowedBook() {
    final Integer ID = 1;
    when(bookRepository.findById(ID)).thenReturn(Optional.of(book1));
    when(bookRepository.isBookBorrowed(ID)).thenReturn(true);
    assertThrows(BookIsBorrowedException.class, () -> bookService.delete(ID));
    verify(bookRepository, times(1)).findById(ID);
    verify(bookRepository, times(0)).deleteById(ID);
  }

  @Test
  public void shouldFindAllBorrowedBooks() {
    when(bookRepository.findAllBorrowedBooks()).thenReturn(
        List.of(book1.getTitle(), book2.getTitle()));
    List<String> responseData = bookService.findAllBorrowedBooks();
    assertNotNull(responseData);
    assertEquals(2, responseData.size());
    responseData = responseData.stream()
        .distinct()
        .toList();
    assertEquals(2, responseData.size());
    verify(bookRepository, times(1)).findAllBorrowedBooks();
  }

  @Test
  public void shouldShowInfoOfBorrowedBooks() {
    final BorrowedBookDto borrowedBookDto = new BorrowedBookDto(book1.getId(), book1.getTitle(), 2);
    when(bookRepository.showInfoOfBorrowedBooks()).thenReturn(List.of(borrowedBookDto));
    List<BorrowedBookDto> responseData = bookService.showInfoOfBorrowedBooks();
    assertNotNull(responseData);
    assertEquals(1, responseData.size());
    verify(bookRepository, times(1)).showInfoOfBorrowedBooks();
  }
}
