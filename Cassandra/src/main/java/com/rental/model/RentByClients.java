package com.rental.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.time.LocalDateTime;

@Entity(defaultKeyspace = "car_rental")
@CqlName("rent_by_clients")
public class RentByClients extends Rent{


    @PartitionKey
    @CqlName("client_id")
    private String clientId;

    @ClusteringColumn
    @CqlName("rent_id")
    private String rentId;

    @CqlName("vehicle_id")
    private String vehicleId;

    @CqlName("begin_time")
    private LocalDateTime beginTime;

    @CqlName("end_time")
    private LocalDateTime endTime;

    @CqlName("rent_cost")
    private double rentCost = 0.0;

    @CqlName("archive")
    private boolean archive = false;

    public RentByClients(String id,String clientId, String vehicleId) {
        super(id, clientId, vehicleId);
    }

    public RentByClients(String id, Client client, Vehicle vehicle) {
        super(id, client, vehicle);
    }

    public RentByClients(String rentId, String clientId, String vehicleId, LocalDateTime beginTime, LocalDateTime endTime, double rentCost, boolean archive) {
        super(rentId, clientId, vehicleId, beginTime, endTime, rentCost, archive);
    }

    public RentByClients() {
        super();
    }

    @Override
    public String getRentId() {
        return rentId;
    }

    @Override
    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getVehicleId() {
        return vehicleId;
    }

    @Override
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public double getRentCost() {
        return rentCost;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    @Override
    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

}
