package com.rental.model;

public class GoldClientType extends ClientType {

    @Override
    public int getMaxVehicles() {
        return 4;
    }

    @Override
    public double applyDiscount(double price) {
        return price * 0.95;
    }
}
