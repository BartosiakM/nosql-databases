package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "car_rental")
@CqlName("vehicle")
public class Bicycle extends Vehicle {

    public Bicycle(String id,String plateNumber,int basePrice, boolean available) {
        super(id, plateNumber, basePrice, available);
        this.discriminator = "bicycle";
    }

    public Bicycle() {
        super();
        this.discriminator = "bicycle";
    }


    @Override
    public double getActualRentalPrice() {
        return getBasePrice();
    }
}
