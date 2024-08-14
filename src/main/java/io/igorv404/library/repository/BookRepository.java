package io.igorv404.library.repository;

import io.igorv404.library.dto.response.BorrowedBookDto;
import io.igorv404.library.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findByTitleAndAuthor(String title, String author);

  @Query("SELECT COUNT(m) != 0 FROM Member m JOIN m.borrowedBooks bb WHERE bb.id = :id")
  boolean isBookBorrowed(@Param("id") Integer id);

  @Query("SELECT DISTINCT b.title FROM Book b INNER JOIN Member m ON b MEMBER OF m.borrowedBooks")
  List<String> findAllBorrowedBooks();

  @Query("""
      SELECT new io.igorv404.library.dto.response.BorrowedBookDto(
        b.id, b.title, CAST(COUNT(DISTINCT m) AS INTEGER)
      ) FROM Book b
      INNER JOIN Member m ON b MEMBER OF m.borrowedBooks
      GROUP BY b.id
      """)
  List<BorrowedBookDto> showInfoOfBorrowedBooks();
}
