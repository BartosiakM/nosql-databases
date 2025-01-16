package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "car_rental")
@CqlName("vehicle")
public class Vehicle {

    @PartitionKey
    @CqlName("vehicle_id")
    private String vehicleId;

    @CqlName("plate_number")
    private String plateNumber;

    @CqlName("base_price")
    private int basePrice;

    @CqlName("available")
    private boolean available;

    @CqlName("discriminator")
    protected String discriminator;

    public Vehicle(String id, String plateNumber, int basePrice, boolean available) {
        this.vehicleId = id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.discriminator = "vehicle";
        this.available = available;
    }

    public Vehicle() {
        this.discriminator = "vehicle";
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String id) {
        this.vehicleId = id;
    }

    public String getPlateNumber() {
        return this.plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public double getActualRentalPrice() {
        return 0;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) { this.available = available;}
}
