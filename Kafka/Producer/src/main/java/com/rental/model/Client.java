package com.rental.model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

public class Client implements Serializable {

    @BsonId
    private long id;

    @BsonProperty("username")
    private String username;

    @BsonProperty("type")
    private ClientType clientType;

    @BsonProperty("activeRents")
    private int activeRents;

    @BsonCreator
    public Client(@BsonId long id,
                  @BsonProperty("username") String username,
                  @BsonProperty("type") ClientType clientType) {
        this.id = id;
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

    public long getId() {
        return this.id;
    }

    public int getMaxVehicles() {
        return clientType.getMaxVehicles();
    }

    public double applyDiscount(double price) {
        return clientType.applyDiscount(price);
    }
}
