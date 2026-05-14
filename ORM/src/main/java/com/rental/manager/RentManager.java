package com.rental.manager;

import com.rental.model.Rent;
import com.rental.repository.RentRepository;
import java.io.Serializable;

public class RentManager implements Serializable {
    private final RentRepository rentRepository;

    public RentManager(RentRepository rentRepository) {
        if (rentRepository == null) {
            throw new NullPointerException("rentRepository is null");
        }
        this.rentRepository = rentRepository;
    }

    public Rent rentVehicle(Rent rent) {
        if (rentRepository.getByID(rent.getRentId()) != null) {
            throw new IllegalArgumentException("Rent already exists");
        }
        rentRepository.add(rent);
        return rent;
    }

    public void endRent(Rent rent) {
        Rent endedRent = rentRepository.getByID(rent.getRentId());
        endedRent.endRent();
        rentRepository.update(endedRent);
    }
}
