package io.igorv404.library.service;

import io.igorv404.library.dto.response.BorrowedBookDto;
import io.igorv404.library.exception.BookIsBorrowedException;
import io.igorv404.library.repository.BookRepository;
import io.igorv404.library.model.Book;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;

  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  public Book findById(Integer id) {
    return bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Book create(Book entity) {
    Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(entity.getTitle(), entity.getAuthor());
    if (existingBook.isPresent()) {
      entity = existingBook.get();
      entity.setAmount(entity.getAmount() + 1);
    }
    return bookRepository.save(entity);
  }

  public Book update(Book entity) {
    findById(entity.getId());
    return bookRepository.save(entity);
  }

  public String delete(Integer id) {
    Book existingBook = findById(id);
    if (bookRepository.isBookBorrowed(id)) {
      throw new BookIsBorrowedException();
    }
    bookRepository.deleteById(id);
    return String.format("Book \"%s\" was deleted", existingBook.getTitle());
  }

  public List<String> findAllBorrowedBooks() {
    return bookRepository.findAllBorrowedBooks();
  }

  public List<BorrowedBookDto> showInfoOfBorrowedBooks() {
    return bookRepository.showInfoOfBorrowedBooks();
  }
}
