package com.rental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Access(AccessType.FIELD)
public abstract class Vehicle extends AbstractEntity implements Serializable {

    @Column(name = "plateNumber")
    @NotNull
    private String plateNumber;

    @Column(name = "basePrice")
    private int basePrice;

    @Column(name = "available")
    private boolean available = true;

    public Vehicle(String plateNumber, int basePrice) {
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
    }

    public Vehicle() {}

    public Long getVehicleId() {
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
