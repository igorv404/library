package io.igorv404.library.util;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ControllerTemplate<T, ID> {
  ResponseEntity<List<T>> findAll();

  ResponseEntity<T> findById(ID id);

  ResponseEntity<T> create(T entity);

  ResponseEntity<T> update(T entity);

  ResponseEntity<String> delete(ID id);
}
