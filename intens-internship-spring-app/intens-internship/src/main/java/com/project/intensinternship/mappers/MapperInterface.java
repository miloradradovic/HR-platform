package com.project.intensinternship.mappers;

import java.util.List;

public interface MapperInterface<T,U> {

    T toEntity(U dto);

    U toDto(T entity);

    List<U> toListDtos(List<T> entities);
}