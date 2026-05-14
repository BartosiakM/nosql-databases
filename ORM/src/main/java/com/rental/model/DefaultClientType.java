package com.rental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("DEFAULT")
public class DefaultClientType extends ClientType {

    @Override
    public int getMaxVehicles() {
        return 1;
    }

    @Override
    public double applyDiscount(double price) {
        return price;
    }
}
