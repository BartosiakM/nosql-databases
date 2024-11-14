package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "ClientType", value = "gold")
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
