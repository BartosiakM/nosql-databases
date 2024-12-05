package com.rental.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@BsonDiscriminator(key = "vehicleType", value = "motorVehicle")
public class MotorVehicle extends Vehicle {

    @BsonProperty("engineDisplacement")
    @JsonbProperty("engineDisplacement")
    private int engineDisplacement;

    @BsonCreator
    @JsonbCreator
    public MotorVehicle(@BsonProperty("id") @JsonbProperty("vehicleId") long id,
                        @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                        @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice,
                        @BsonProperty("engineDisplacement")  @JsonbProperty("engineDisplacement") int engineDisplacement) {
        super(id, plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(int engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    @Override
    public double getActualRentalPrice() {
        double rentalPrice = getBasePrice();
        if (engineDisplacement > 2000) {
            rentalPrice *= 1.5;
        } else if (engineDisplacement > 1000) {
            rentalPrice *= (engineDisplacement * 0.5) / 1000.0 + 0.5;
        }
        return rentalPrice;
    }
}
