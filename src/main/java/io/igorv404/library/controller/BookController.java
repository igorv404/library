package io.igorv404.library.controller;

import io.igorv404.library.dto.response.BorrowedBookDto;
import io.igorv404.library.model.Book;
import io.igorv404.library.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping
  public ResponseEntity<List<Book>> findAll() {
    return ResponseEntity.ok(bookService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(bookService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Book> create(@Valid @RequestBody Book entity) {
    return new ResponseEntity<>(bookService.create(entity), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<Book> update(@Valid @RequestBody Book entity) {
    return new ResponseEntity<>(bookService.update(entity), HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Integer id) {
    return new ResponseEntity<>(bookService.delete(id), HttpStatus.ACCEPTED);
  }

  @GetMapping("/showBorrowedBooks")
  public ResponseEntity<List<String>> findAllBorrowedBooks() {
    return ResponseEntity.ok(bookService.findAllBorrowedBooks());
  }

  @GetMapping("/showBorrowedBooksInDetails")
  public ResponseEntity<List<BorrowedBookDto>> showInfoOfBorrowedBooks() {
    return ResponseEntity.ok(bookService.showInfoOfBorrowedBooks());
  }
}
