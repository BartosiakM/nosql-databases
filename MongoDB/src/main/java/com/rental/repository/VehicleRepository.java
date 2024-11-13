package com.rental.repository;

import com.mongodb.client.model.Filters;
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

    // Dodawanie nowego pojazdu
    public void add(Vehicle vehicle) {
        getDatabase().getCollection("vehicles", Vehicle.class).insertOne(vehicle);
    }

    // Znajdowanie pojazdu po ID
    public Optional<Vehicle> findById(UUID id) {
        return Optional.ofNullable(getDatabase().getCollection("vehicles", Vehicle.class)
                .find(eq("_id", id)).first());
    }

    // Znajdowanie wszystkich pojazdów
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        getDatabase().getCollection("vehicles", Vehicle.class).find().into(vehicles);
        return vehicles;
    }

    // Aktualizacja informacji o pojeździe
    public void update(Vehicle vehicle) {
        Bson updates = Updates.combine(
                set("plateNumber", vehicle.getPlateNumber()),
                set("basePrice", vehicle.getBasePrice()),
                set("available", vehicle.isAvailable())
        );

        getDatabase().getCollection("vehicles", Vehicle.class)
                .updateOne(eq("_id", vehicle.getVehicleId()), updates);
    }

    // Usuwanie pojazdu po ID
    public void delete(UUID id) {
        getDatabase().getCollection("vehicles", Vehicle.class).deleteOne(eq("_id", id));
    }
}
