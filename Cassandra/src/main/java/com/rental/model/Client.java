package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "car_rental")
@CqlName("clients")
public class Client {

    @PartitionKey
    @CqlName("client_id")
    private String clientId;

    @CqlName("username")
    private String username;

    @CqlName("client_type")
    private ClientType clientType;

    @CqlName("activeRents")
    private int activeRents;


    public Client(String id, String username, ClientType clientType) {
        this.clientId = id;
        this.username = username;
        this.clientType = clientType;
        this.activeRents = 0;
    }

    public Client() {}


    public ClientType getClientType() {
        return this.clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
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

    public String getClientId() {
        return this.clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getMaxVehicles() {
        return getClientType().getMaxVehicles();
    }

    public double applyDiscount(double price) {
        return getClientType().applyDiscount(price);
    }
}
