package com.rental.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.rental.model.Vehicle;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class VehicleRepository extends AbstractMongoRepository {

    private final MongoCollection<Vehicle> vehicleCollection;

    // Inicjalizacja kolekcji w konstruktorze
    public VehicleRepository() {
        this.vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
    }

    // Dodawanie nowego pojazdu
    public Vehicle add(Vehicle vehicle) {
        vehicleCollection.insertOne(vehicle);
        return vehicle;
    }

    // Znajdowanie pojazdu po ID
    public Optional<Vehicle> findById(UUID id) {
        return Optional.ofNullable(vehicleCollection.find(eq("id", id)).first());
    }

    // Znajdowanie wszystkich pojazdów
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleCollection.find().into(vehicles);
        return vehicles;
    }

    // Aktualizacja informacji o pojeździe
    public void update(Vehicle vehicle) {
        Bson updates = Updates.combine(
                set("plateNumber", vehicle.getPlateNumber()),
                set("basePrice", vehicle.getBasePrice()),
                set("available", vehicle.isAvailable())
        );
        vehicleCollection.updateOne(eq("id", vehicle.getVehicleId()), updates);
    }

    // Usuwanie pojazdu po ID
    public void delete(UUID id) {
        vehicleCollection.deleteOne(eq("id", id));
    }
}
