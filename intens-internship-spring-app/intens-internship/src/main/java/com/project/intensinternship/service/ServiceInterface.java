package com.project.intensinternship.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceInterface<T> {

    List<T> findAll();

    T findOne(int id);

    T saveOne(T entity);

    boolean delete(int id);

    T update(T entity);

}
