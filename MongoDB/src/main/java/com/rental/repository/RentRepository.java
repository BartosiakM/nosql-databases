package com.rental.repository;

import com.mongodb.client.MongoCollection;
import com.rental.model.Client;
import com.rental.model.Rent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class RentRepository extends AbstractMongoRepository {

    private final MongoCollection<Rent> rentCollection;

    // Inicjalizacja kolekcji w konstruktorze
    public RentRepository() {
        this.rentCollection = getDatabase().getCollection("rents", Rent.class);
    }

    public Optional<Rent> findById(UUID id) {
        return Optional.ofNullable(rentCollection.find(eq("id", id)).first());
    }

    // Znajdowanie wszystkich klientów
    public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        rentCollection.find().into(rents);
        return rents;
    }
}