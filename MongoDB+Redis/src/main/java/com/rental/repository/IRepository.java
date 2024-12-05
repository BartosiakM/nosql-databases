package com.rental.repository;

import com.rental.model.Vehicle;

import java.util.List;

public interface IRepository {
    Vehicle add(Vehicle vehicle);
    Vehicle findById(long id);
    List<Vehicle> findAll();
    void update(Vehicle vehicle);
    void delete(long id);
}