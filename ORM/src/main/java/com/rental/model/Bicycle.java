package com.rental.model;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Bicycle")
public class Bicycle extends Vehicle {

    public Bicycle(String plateNumber, int basePrice) {
        super(plateNumber, basePrice);
    }

    public Bicycle() {
        super("", 0);
    }

    @Override
    public double getActualRentalPrice() {
        return getBasePrice();
    }
}
