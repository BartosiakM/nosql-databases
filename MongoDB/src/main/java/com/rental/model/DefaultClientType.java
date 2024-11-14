package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "ClientType", value = "default")
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
