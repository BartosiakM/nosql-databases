package com.rental.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "vehicleType", value = "bicycle")
public class Bicycle extends Vehicle {

    @BsonCreator
    @JsonbCreator
    public Bicycle(@BsonProperty("id") @JsonbProperty("vehicleId") long id,
                   @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                   @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice) {
        super(id, plateNumber, basePrice);
    }


    @Override
    public double getActualRentalPrice() {
        return getBasePrice();
    }
}
