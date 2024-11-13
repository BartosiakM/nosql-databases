package com.rental.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

@BsonDiscriminator(key = "vehicle_type")
public abstract class Vehicle implements Serializable {

    @BsonId
    private UUID id;

    @BsonProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("basePrice")
    private int basePrice;

    @BsonProperty("available")
    private boolean available = true;

    @BsonCreator
    public Vehicle(@BsonId UUID id,
                   @BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("basePrice") int basePrice) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
    }

    public UUID getVehicleId() {
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

    public abstract double getActualRentalPrice();

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
