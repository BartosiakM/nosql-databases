package com.rental.model;

import java.time.LocalDateTime;

public class Rent {

    private String rentId;

    private String clientId;

    private String vehicleId;

    private Client client;

    private Vehicle vehicle;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private double rentCost = 0.0;

    private boolean archive = false;

    public Rent(String id, Client client, Vehicle vehicle) {
        this.rentId = id;
        this.client = client;
        this.vehicle = vehicle;
        this.beginTime = LocalDateTime.now();
        this.clientId = client.getClientId();
        this.vehicleId = vehicle.getVehicleId();
    }

    public Rent(String id, String clientId, String vehicleId) {
        this.rentId = id;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.beginTime = LocalDateTime.now();
    }

    public Rent(String rentId, String clientId, String vehicleId, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archive) {
        this.rentId = rentId;
        this.clientId = clientId;
        this.vehicleId = vehicleId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.rentCost = rentCost;
        this.archive = archive;
    }

    public Rent() {}

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getClientId() {
        return clientId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setClientIs(String clientId) {
        this.clientId = clientId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRentId() {
        return this.rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
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
        this.rentCost = client.applyDiscount(getRentDays() * vehicle.getActualRentalPrice());
    }

    public double getRentCost() {
        return rentCost;
    }

    public boolean isArchive() { return archive; }
}
