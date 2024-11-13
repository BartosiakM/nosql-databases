package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_type")
public abstract class ClientType{

    public abstract int getMaxVehicles();

    public abstract double applyDiscount(double price);
}
