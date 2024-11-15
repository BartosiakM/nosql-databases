package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "type", value = "bronze")
public class BronzeClientType extends ClientType {
    @Override
    public int getMaxVehicles() {
        return 2;
    }

    @Override
    public double applyDiscount(double price) {
        double discount = 3.0;
        return (price > discount) ? price - discount : price;
    }
}
