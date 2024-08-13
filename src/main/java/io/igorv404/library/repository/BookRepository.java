package io.igorv404.library.repository;

import io.igorv404.library.model.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findByTitleAndAuthor(String title, String author);
}
