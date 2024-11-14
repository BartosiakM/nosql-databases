package com.rental.manager;

import com.rental.model.Vehicle;
import com.rental.repository.VehicleRepository;

import java.util.UUID;

public class VehicleManager {
    private final VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new NullPointerException("vehicleRepository is null");
        }
        this.vehicleRepository = vehicleRepository;
    }

    public void removeVehicle(UUID ID) {
        vehicleRepository.delete(ID);
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicleRepository.findById(vehicle.getVehicleId()).isPresent()) {
            throw new IllegalArgumentException("vehicle already exists");
        }
        return vehicleRepository.add(vehicle);
    }
}
