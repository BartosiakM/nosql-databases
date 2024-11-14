package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Rent implements Serializable {

    @BsonId
    private UUID id;

    @BsonProperty("client")
    private Client client;

    @BsonProperty("vehicle")
    private Vehicle vehicle;

    @BsonProperty("beginTime")
    private LocalDateTime beginTime;

    @BsonProperty("endTime")
    private LocalDateTime endTime;

    @BsonProperty("rentCost")
    private double rentCost = 0.0;

    @BsonProperty("archive")
    private boolean archive = false;

    @BsonCreator
    public Rent(@BsonId UUID id,
                @BsonProperty("client") Client client,
                @BsonProperty("vehicle") Vehicle vehicle) {
        this.id = id;
        this.client = client;
        this.vehicle = vehicle;
        this.beginTime = LocalDateTime.now();
    }

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public UUID getRentId() {
        return this.id;
    }

    public int getRentDays() {
        if (endTime == null) return 0;
        return (int) java.time.Duration.between(beginTime, endTime).toDays() + 1;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void endRent() {
        if (this.endTime != null) {
            throw new IllegalStateException("Rent has already ended");
        }
        this.endTime = LocalDateTime.now();
        this.archive = true;
        this.vehicle.setAvailable(true);
        this.client.setActiveRents(client.getActiveRents() - 1);
        this.rentCost = client.applyDiscount(getRentDays() * vehicle.getActualRentalPrice());
    }

    public double getRentCost() {
        return rentCost;
    }
}
