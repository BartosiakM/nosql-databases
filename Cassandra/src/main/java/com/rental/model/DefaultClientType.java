package com.rental.model;

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
