package com.rental.dao;
import com.datastax.oss.driver.api.mapper.annotations.*;
import com.rental.model.Bicycle;
import com.rental.model.Car;
import com.rental.model.MotorVehicle;
import com.rental.model.Vehicle;
import com.rental.provider.VehicleProvider;

@Dao
public interface VehicleDao {
    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, MotorVehicle.class, Vehicle.class, Car.class})
    Vehicle findById(String id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, MotorVehicle.class, Vehicle.class, Car.class})
    Vehicle add(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VehicleProvider.class, entityHelpers = {Bicycle.class, MotorVehicle.class, Vehicle.class, Car.class})
    void update(Vehicle vehicle);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Delete
    void delete(Vehicle vehicle);
}