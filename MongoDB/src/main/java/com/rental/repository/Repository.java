package com.rental.repository;

import java.util.List;

public interface Repository<T> {

    T getByID(Long id);
    List<T> findAll();
    T add(T t);
    void remove(T t);
    void update(T t);
}
