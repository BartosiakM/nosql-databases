package com.rental.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.rental.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class ClientRepository extends AbstractMongoRepository {

    // Dodawanie nowego klienta
    public void add(Client client) {
        getDatabase().getCollection("clients", Client.class).insertOne(client);
    }

    // Znajdowanie klienta po ID
    public Optional<Client> findById(UUID id) {
        return Optional.ofNullable(getDatabase().getCollection("clients", Client.class)
                .find(eq("_id", id)).first());
    }

    // Znajdowanie wszystkich klientów
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        getDatabase().getCollection("clients", Client.class).find().into(clients);
        return clients;
    }

    // Aktualizacja informacji o kliencie
    public void update(Client client) {
        getDatabase().getCollection("clients", Client.class).updateOne(
                eq("_id", client.getClientId()),
                Updates.combine(
                        set("firstName", client.getUsername()),
                        set("clientType", client.getClientType())
                )
        );
    }

    // Usuwanie klienta po ID
    public void delete(UUID id) {
        getDatabase().getCollection("clients", Client.class).deleteOne(eq("_id", id));
    }
}
