package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "car_rental")
@CqlName("vehicle")
public class Car extends Vehicle {

    @CqlName("engine_displacement")
    private int engineDisplacement;

    @CqlName("segment")
    private String segment;

    public Car(String id, String plateNumber, int basePrice, boolean available, int engineDisplacement,String segment) {
        super(id, plateNumber, basePrice, available);
        this.engineDisplacement = engineDisplacement;
        this.segment = segment;
        this.discriminator = "car";
    }

    public Car() {
        super();
        this.discriminator = "car";
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
