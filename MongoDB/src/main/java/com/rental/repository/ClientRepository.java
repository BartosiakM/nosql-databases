package com.rental.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.rental.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class ClientRepository extends AbstractMongoRepository {

    private final MongoCollection<Client> clientCollection;

    // Inicjalizacja kolekcji w konstruktorze
    public ClientRepository() {
        this.clientCollection = getDatabase().getCollection("clients", Client.class);
    }

    // Dodawanie nowego klienta
    public Client add(Client client) {
        clientCollection.insertOne(client);
        return client;
    }

    // Znajdowanie klienta po ID
    public Optional<Client> findById(UUID id) {
        return Optional.ofNullable(clientCollection.find(eq("id", id)).first());
    }

    // Znajdowanie wszystkich klientów
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        clientCollection.find().into(clients);
        return clients;
    }

    // Aktualizacja informacji o kliencie
    public void update(Client client) {
        clientCollection.updateOne(
                eq("id", client.getClientId()),
                Updates.combine(
                        set("firstName", client.getUsername()),
                        set("clientType", client.getClientType())
                )
        );
    }

    // Usuwanie klienta po ID
    public void delete(UUID id) {
        clientCollection.deleteOne(eq("id", id));
    }
}
