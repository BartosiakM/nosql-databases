package com.rental.model;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Car")
public class Car extends Vehicle {

    @Column(name="engine_displacement")
    private int engineDisplacement;

    @Column(name = "segment")
    private String segment;

    public Car(String plateNumber, int basePrice, int engineDisplacement, String segment) {
        super(plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
        this.segment = segment;
    }

    public Car() {
        super("", 0);
    }

    public String getSegment() {
        return segment;
    }

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
