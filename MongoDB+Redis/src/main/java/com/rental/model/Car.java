package com.rental.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "vehicleType", value = "car")
public class Car extends Vehicle {

    @BsonProperty("engineDisplacement")
    @JsonbProperty("engineDisplacement")
    private int engineDisplacement;

    @BsonProperty("segment")
    @JsonbProperty("segment")
    private String segment;

    @BsonCreator
    @JsonbCreator
    public Car(@BsonProperty("id") @JsonbProperty("vehicleId")long id,
               @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
               @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice,
               @BsonProperty("engineDisplacement") @JsonbProperty("engineDisplacement") int engineDisplacement,
               @BsonProperty("segment") @JsonbProperty("segment") String segment) {
        super(id, plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
        this.segment = segment;
    }

    public String getSegment() {
        return segment;
    }

    public int getEngineDisplacement() { return this.engineDisplacement; }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Override
    public double getActualRentalPrice() {
        double multiplier;

        if (segment.equalsIgnoreCase("A")) {
            multiplier = 1.0;
        } else if (segment.equalsIgnoreCase("B")) {
            multiplier = 1.1;
        } else if (segment.equalsIgnoreCase("C")) {
            multiplier = 1.2;
        } else if (segment.equalsIgnoreCase("D")) {
            multiplier = 1.3;
        } else if (segment.equalsIgnoreCase("E")) {
            multiplier = 1.4;
        } else {
            multiplier = 1.0;
        }

        return getBasePrice() * multiplier;
    }
}
