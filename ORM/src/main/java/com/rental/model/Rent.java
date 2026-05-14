package com.rental.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Rent")
@Access(AccessType.FIELD)
public class Rent extends AbstractEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    @NotNull
    private Vehicle vehicle;

    @Column(name = "begine_time")
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "rent_cost")
    private double rentCost = 0.0;

    @Column(name = "archive")
    private boolean isArchive = false;

    public Rent(Client client, Vehicle vehicle) {
        this.client = client;
        this.vehicle = vehicle;
        this.beginTime = LocalDateTime.now();
    }

    public Rent() {}

    public Client getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long getRentId() {
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
        this.isArchive = true;
        this.vehicle.setAvailable(true);
        this.client.setActiveRents(client.getActiveRents() - 1);
        this.rentCost = client.applyDiscount(getRentDays() * vehicle.getActualRentalPrice());
    }

    public double getRentCost() {
        return rentCost;
    }
}
