package com.rental.manager;

import com.rental.model.Vehicle;
import com.rental.repository.MongoVehicleRepository;

public class VehicleManager {
    private final MongoVehicleRepository vehicleRepository;

    public VehicleManager(MongoVehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new NullPointerException("vehicleRepository is null");
        }
        this.vehicleRepository = vehicleRepository;
    }

    public void removeVehicle(long ID) {
        vehicleRepository.delete(ID);
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicleRepository.findById(vehicle.getId()) != null) {
            throw new IllegalArgumentException("vehicle already exists");
        }
        return vehicleRepository.add(vehicle);
    }
}
