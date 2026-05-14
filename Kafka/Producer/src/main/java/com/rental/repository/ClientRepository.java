package com.rental.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.rental.model.Client;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class ClientRepository extends AbstractMongoRepository {

    private final MongoCollection<Client> clientCollection;

    public ClientRepository() {
        this.clientCollection = getDatabase().getCollection("clients", Client.class);
    }

    public Client add(Client client) {
        clientCollection.insertOne(client);
        return client;
    }

    public Client findById(long id) {
        return clientCollection.find(eq("_id", id)).first();
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        clientCollection.find().into(clients);
        return clients;
    }

    public void update(Client client) {
        clientCollection.updateOne(
                eq("_id", client.getId()),
                Updates.combine(
                        set("username", client.getUsername()),
                        set("clientType", client.getClientType())
                )
        );
    }

    public void delete(long id) {
        clientCollection.deleteOne(eq("_id", id));
    }
}
