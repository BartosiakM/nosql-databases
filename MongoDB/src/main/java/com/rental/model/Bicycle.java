package com.rental.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "vehicleType", value = "bicycle")
public class Bicycle extends Vehicle {

    @BsonCreator
    public Bicycle(@BsonProperty("id") long id,
                   @BsonProperty("plateNumber") String plateNumber,
                   @BsonProperty("basePrice") int basePrice) {
        super(id, plateNumber, basePrice);
    }


    @Override
    public double getActualRentalPrice() {
        return getBasePrice();
    }
}
