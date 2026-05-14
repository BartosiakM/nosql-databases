package com.rental.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class RentToSend {
    private Rent rent;
    private String rentalName;

    @JsonCreator
    public RentToSend(@JsonbProperty("rent") Rent rent,@JsonbProperty("rentalName") String rentalName) {
        this.rent = rent;
        this.rentalName = rentalName;
    }
    public RentToSend() {}

    public Rent getRent() {
        return rent;
    }
    public void setRent(Rent rent) {
        this.rent = rent;
    }
    public String getRentalName() {
        return rentalName;
    }
    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }
}
