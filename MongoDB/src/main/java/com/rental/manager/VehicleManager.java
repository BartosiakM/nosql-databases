package com.rental.manager;

import com.rental.model.Vehicle;
import com.rental.repository.VehicleRepository;

public class VehicleManager {
    private final VehicleRepository vehicleRepository;

    public VehicleManager(VehicleRepository vehicleRepository) {
        if (vehicleRepository == null) {
            throw new NullPointerException("vehicleRepository is null");
        }
        this.vehicleRepository = vehicleRepository;
    }

    public void removeVehicle(Long ID) {
        vehicleRepository.remove(vehicleRepository.getByID(ID));
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicleRepository.getByID(vehicle.getVehicleId()) != null) {
            throw new IllegalArgumentException("vehicle already exists");
        }
        return vehicleRepository.add(vehicle);
    }
}
