package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.UUID;

public class Client implements Serializable {

    @BsonId
    private final UUID id;

    @BsonProperty("username")
    private String username;

    @BsonProperty("clientType")
    private ClientType clientType;

    private int activeRents;

    @BsonCreator
    public Client(@BsonId UUID id,
                  @BsonProperty("username") String username,
                  @BsonProperty("clientType") ClientType clientType) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.username = username;
        this.clientType = clientType;
        this.activeRents = 0;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public int getActiveRents() {
        return activeRents;
    }

    public void setActiveRents(int activeRents) {
        this.activeRents = activeRents;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getClientId() {
        return this.id;
    }

    public int getMaxVehicles() {
        return clientType.getMaxVehicles();
    }

    public double applyDiscount(double price) {
        return clientType.applyDiscount(price);
    }
}
