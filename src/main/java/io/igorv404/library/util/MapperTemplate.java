package io.igorv404.library.util;

import java.util.List;

public interface MapperTemplate<T, D> {
  T toEntity(D dto);

  D toDto(T entity);

  List<T> toEntityList(List<D> dtoList);

  List<D> toDtoList(List<T> entityList);
}
