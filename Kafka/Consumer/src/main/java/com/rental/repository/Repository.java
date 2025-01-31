package com.rental.repository;

import ch.qos.logback.core.net.server.Client;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Repository extends AbstractMongoRepository {
    private final MongoCollection<Document> rentCollection;


    public Repository() {
        this.rentCollection = getDatabase().getCollection("rents");
    }

    public void add(String message) {
        try (ClientSession session = getMongoClient().startSession()) {
            session.startTransaction();
            try {
                Document doc = new Document().append("rents", message);
                rentCollection.insertOne(doc);
                System.out.println("Rent added: " + message);

            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            } finally {
                session.close();
            }
        }
    }


}
