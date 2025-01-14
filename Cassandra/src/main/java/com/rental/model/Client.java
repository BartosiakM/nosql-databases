package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "car_rental")
public class Client {

    @PartitionKey
    @CqlName("client_id")
    private long id;

    @CqlName("username")
    private String username;

    @CqlName("type")
    private ClientType clientType;

    @CqlName("activeRents")
    private int activeRents;


    public Client(long id, String username, ClientType clientType) {
        this.id = id;
        this.username = username;
        this.clientType = clientType;
        this.activeRents = 0;
    }

    public Client() {}

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
