package io.igorv404.library.util;

import java.util.List;

public interface ServiceTemplate<T, ID> {
  List<T> findAll();

  T findById(ID id);

  T create(T entity);

  T update(T entity);

  String delete(ID id);
}
