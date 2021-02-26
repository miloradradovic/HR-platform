package com.project.intensinternship.service;

import java.util.List;

public interface ServiceInterface<T> {

    List<T> findAll();

    T findOne(int id);

    T saveOne(T entity);

    boolean delete(int id);

    T update(T entity);

}
