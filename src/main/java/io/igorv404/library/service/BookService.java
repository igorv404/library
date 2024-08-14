package io.igorv404.library.service;

import io.igorv404.library.exception.BookIsBorrowedException;
import io.igorv404.library.repository.BookRepository;
import io.igorv404.library.util.ServiceTemplate;
import io.igorv404.library.model.Book;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService implements ServiceTemplate<Book, Integer> {
  private final BookRepository bookRepository;

  @Override
  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  @Override
  public Book findById(Integer id) {
    return bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public Book create(Book entity) {
    Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(entity.getTitle(), entity.getAuthor());
    if (existingBook.isPresent()) {
      entity = existingBook.get();
      entity.setAmount(entity.getAmount() + 1);
    }
    return bookRepository.save(entity);
  }

  @Override
  public Book update(Book entity) {
    Book existingBook = findById(entity.getId());
    existingBook.setTitle(entity.getTitle());
    existingBook.setAuthor(entity.getAuthor());
    existingBook.setAmount(entity.getAmount());
    return bookRepository.save(existingBook);
  }

  @Override
  public String delete(Integer id) {
    Book existingBook = findById(id);
    if (bookRepository.isBookBorrowed(id)) {
      throw new BookIsBorrowedException();
    }
    bookRepository.deleteById(id);
    return String.format("Book \"%s\" was deleted", existingBook.getTitle());
  }
}
