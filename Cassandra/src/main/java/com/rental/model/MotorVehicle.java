package com.rental.model;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "car_renatal")
@CqlName("vehicle")
public class MotorVehicle extends Vehicle {

    @CqlName("engineDisplacement")
    private int engineDisplacement;

    public MotorVehicle(long id,String plateNumber,int basePrice, int engineDisplacement, boolean available) {
        super(id, plateNumber, basePrice,available);
        this.discriminator = "motorVehicle";
        this.engineDisplacement = engineDisplacement;
    }

    public MotorVehicle() {
        super();
        this.discriminator = "motorVehicle";
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(int engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = getBasePrice();
        if (engineDisplacement > 2000) {
            rentalPrice *= 1.5;
        } else if (engineDisplacement > 1000) {
            rentalPrice *= (engineDisplacement * 0.5) / 1000.0 + 0.5;
        }
        return rentalPrice;
    }
}
