package com.rental.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BRONZE")
public class BronzeClientType extends ClientType {
    @Override
    public int getMaxVehicles() {
        return 2;
    }

    @Override
    public double applyDiscount(double price) {
        double discount = 3.0;
        return (price > discount) ? price - discount : price;
    }

    @Override
    public String getTypeInfo() {
        return "Bronze 2 $3.00";
    }
}
