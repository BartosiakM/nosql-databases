package com.rental.model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

@BsonDiscriminator(key = "vehicleType")
@JsonbTypeInfo({
        @JsonbSubtype(alias = "BICYCLE", type = Bicycle.class),
        @JsonbSubtype(alias = "CAR", type = Car.class),
        @JsonbSubtype(alias = "MOTOR", type = MotorVehicle.class)
})
public abstract class Vehicle implements Serializable {

    @BsonId
    @JsonbProperty("vehicleId")
    private long id;

    @BsonProperty("plateNumber")
    @JsonbProperty("plateNumber")
    private String plateNumber;

    @BsonProperty("basePrice")
    @JsonbProperty("basePrice")
    private int basePrice;

    @BsonProperty("available")
    @JsonbProperty("available")
    private int available = 0;

    @BsonCreator
    @JsonbCreator
    public Vehicle(@BsonId  @JsonbProperty("vehicleId") long id,
                   @BsonProperty("plateNumber") @JsonbProperty("plateNumber") String plateNumber,
                   @BsonProperty("basePrice") @JsonbProperty("basePrice") int basePrice) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
    }

    public long getId() {
        return this.id;
    }

    public String getPlateNumber() {
        return this.plateNumber;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public abstract double getActualRentalPrice();

    public int isAvailable() {
        return available;
    }

    public void setAvailable(int available) { this.available = available;}
}
