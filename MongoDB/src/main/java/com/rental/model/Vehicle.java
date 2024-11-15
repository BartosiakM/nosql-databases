package com.rental.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

@BsonDiscriminator(key = "vehicleType")
public abstract class Vehicle implements Serializable {

    @BsonId
    private long id;

    @BsonProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("basePrice")
    private int basePrice;

    @BsonProperty("available")
    private int available = 0;

    @BsonCreator
    public Vehicle(@BsonId long id,
                   @BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("basePrice") int basePrice) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
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

    public abstract double getActualRentalPrice();

    public int isAvailable() {
        return available;
    }

    public void setAvailable(int available) { this.available = available;}
}
