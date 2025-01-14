package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "car_rental")
@CqlName("vehicle")
public class Vehicle {

    @PartitionKey
    @CqlName("vehicle_id")
    private long id;

    @CqlName("plateNumber")
    private String plateNumber;

    @CqlName("basePrice")
    private int basePrice;

    @CqlName("available")
    private boolean available;

    @CqlName("discriminator")
    protected String discriminator;

    public Vehicle(long id, String plateNumber, int basePrice, boolean available) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.discriminator = "vehicle";
        this.available = available;
    }

    public Vehicle() {
        this.discriminator = "vehicle";
    }

    public long getId() {
        return this.id;
    }

    public String getPlateNumber() {
        return this.plateNumber;
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
