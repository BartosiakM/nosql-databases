package com.rental.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.client.model.ValidationOptions;
import com.rental.model.Client;
import com.rental.model.ClientTypeCodec;
import com.rental.model.Vehicle;
import com.rental.model.VehicleCodec;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {

    private ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");

    private MongoCredential credential = MongoCredential.createCredential(
            "nbd", "admin", "nbdpassword".toCharArray());

    private CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .register(Client.class)
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build()
    );

    private MongoClient mongoClient;
    private MongoDatabase database;

    private void initDbConnection() {
        CodecRegistry customCodecs = CodecRegistries.fromCodecs(
                new VehicleCodec(MongoClientSettings.getDefaultCodecRegistry()),
                new ClientTypeCodec(MongoClientSettings.getDefaultCodecRegistry())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        customCodecs,
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();

        mongoClient = MongoClients.create(settings);
        database =mongoClient.getDatabase("rental");
        database.getCollection("clients", Client.class).drop();
        database.getCollection("vehicles", Vehicle.class).drop();
        createClientCollection();
        createVehicleCollection();
    }

    public void createClientCollection() {
        MongoDatabase db = getDatabase();

        // Check if the collection already exists
        boolean collectionExists = db.listCollectionNames()
                .into(new ArrayList<>())
                .contains("clients");

        if (!collectionExists) {
            // Define schema validation for the 'clients' collection
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    new Document("$jsonSchema", new Document()
                            .append("bsonType", "object")
                            .append("required", List.of("_id", "username", "type", "activeRents"))
                            .append("properties", new Document()
                                    .append("_id", new Document("bsonType", "long")
                                            .append("description", "must be a unique long identifier"))
                                    .append("username", new Document("bsonType", "string")
                                            .append("description", "must be a string and is required"))
                                    .append("clientType", new Document("bsonType", "object")
                                            .append("description", "must be an embedded document for client type")
                                            .append("properties", new Document()
                                                    .append("type", new Document("bsonType", "string")
                                                            .append("description", "type of client, e.g., 'gold', 'silver', 'default'"))))
                                    .append("activeRents", new Document("bsonType", "int")
                                            .append("description", "number of active rents, must be an integer"))
                            )
                            // Conditional validation on 'activeRents' based on 'clientType.type'
                            .append("oneOf", List.of(
                                    // Condition for 'gold' client type
                                    new Document("properties", new Document("type",
                                            new Document("properties", new Document("type", new Document("enum", List.of("gold"))))
                                    ).append("activeRents", new Document("maximum", 4))),

                                    // Condition for 'diamond' client type
                                    new Document("properties", new Document("type",
                                            new Document("properties", new Document("type", new Document("enum", List.of("diamond"))))
                                    ).append("activeRents", new Document("maximum", 10))),

                                    // Condition for 'default' client type
                                    new Document("properties", new Document("type",
                                            new Document("properties", new Document("type", new Document("enum", List.of("default"))))
                                    ).append("activeRents", new Document("maximum", 1))),

                                    // Condition for 'bronze' client type
                                    new Document("properties", new Document("type",
                                            new Document("properties", new Document("type", new Document("enum", List.of("bronze"))))
                                    ).append("activeRents", new Document("maximum", 2)))


                            ))
                    )
            ).validationAction(ValidationAction.ERROR)
                    .validationLevel(ValidationLevel.STRICT);

            // Create the 'clients' collection with the validation options
            db.createCollection("clients", new CreateCollectionOptions().validationOptions(validationOptions));
        }
    }

    public void createVehicleCollection() {
        MongoDatabase db = getDatabase();

        boolean collectionExists = db.listCollectionNames()
                .into(new ArrayList<>())
                .contains("vehicles");

        if (!collectionExists) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                            new Document("$jsonSchema", new Document()
                                    .append("bsonType", "object")
                                    .append("required", List.of("_id", "vehicleType", "plateNumber", "basePrice", "available"))
                                    .append("properties", new Document()
                                            .append("_id", new Document("bsonType", "long"))
                                            .append("vehicleType", new Document("bsonType", "string"))
                                            .append("plateNumber", new Document("bsonType", "string"))
                                            .append("basePrice", new Document("bsonType", "int"))
                                            .append("available", new Document("bsonType", "int")
                                                    .append("minimum", 0)
                                                    .append("maximum", 1))
                                            .append("engineDisplacement", new Document("bsonType", "int"))
                                            .append("segment", new Document("bsonType", "string"))
                                    )
                            )
                    ).validationAction(ValidationAction.ERROR)
                    .validationLevel(ValidationLevel.STRICT);

            db.createCollection("vehicles", new CreateCollectionOptions().validationOptions(validationOptions));
        }
    }

    public MongoClient getMongoClient() {
        if (mongoClient == null) {
            initDbConnection();
        }
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        if (database == null) {
            initDbConnection();
        }
        return database;
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}