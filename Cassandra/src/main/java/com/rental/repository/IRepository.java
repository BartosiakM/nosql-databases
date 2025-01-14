package com.rental.repository;

import java.util.List;

public interface IRepository<T> {
    T add(T obj);
    T findById(long id);
    //List<T> findAll();
    void update(T obj);
    void delete(T obj);
}