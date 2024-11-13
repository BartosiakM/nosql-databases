package com.rental.model;


import com.sun.istack.NotNull;
import jakarta.persistence.*;
import jakarta.validation.Valid;


import java.util.UUID;

@Entity
@Valid
@Table(name = "Addres")
@Access(AccessType.FIELD)

public class Address extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "Client_ID")
    private UUID ID;

    @Column(nullable = false)
    @NotNull
    private String city;

    @Column(nullable = false)
    @NotNull
    private String street;

    @Column(nullable = false)
    @NotNull
    private String number;

    public Address(String city, String street, String number) {
        this.city = city;
        this.street = street;
        this.number = number;
    }

    public Address() {

    }
}
