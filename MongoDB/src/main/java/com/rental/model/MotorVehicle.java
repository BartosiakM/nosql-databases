package com.rental.model;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("MotorVehicle")
public class MotorVehicle extends Vehicle {

    @Column(name="engine_displacement")
    private int engineDisplacement;

    public MotorVehicle(String plateNumber, int basePrice, int engineDisplacement) {
        super(plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }

    public MotorVehicle() {
        super("", 0);
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
