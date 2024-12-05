package com.rental.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.rental.model.Vehicle;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoVehicleRepository extends AbstractMongoRepository implements IRepository {

    private final MongoCollection<Vehicle> vehicleCollection;


    public MongoVehicleRepository() {
        this.vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
    }

    public Vehicle add(Vehicle vehicle) {
        vehicleCollection.insertOne(vehicle);
        return vehicle;
    }

    public Vehicle findById(long id) {
        return vehicleCollection.find(eq("_id", id)).first();
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleCollection.find().into(vehicles);
        return vehicles;
    }

    public void update(Vehicle vehicle) {
        Bson updates = Updates.combine(
                set("plateNumber", vehicle.getPlateNumber()),
                set("basePrice", vehicle.getBasePrice()),
                set("available", vehicle.isAvailable())
        );
        vehicleCollection.updateOne(eq("_id", vehicle.getId()), updates);
    }

    public void delete(long id) {
        vehicleCollection.deleteOne(eq("_id", id));
    }
}
