package com.rental.model;
public abstract class ClientType{

    public abstract int getMaxVehicles();

    public abstract double applyDiscount(double price);
}
