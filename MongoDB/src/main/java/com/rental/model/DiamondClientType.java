package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "ClientType", value = "diamond")
public class DiamondClientType extends ClientType {

    @Override
    public int getMaxVehicles() {
        return 10;
    }

    @Override
    public double applyDiscount(double price) {
        if (price > 500.0) return 0.6 * price;
        else if (price > 250.0) return 0.7 * price;
        else if (price > 125.0) return 0.8 * price;
        else return 0.9 * price;
    }
}
