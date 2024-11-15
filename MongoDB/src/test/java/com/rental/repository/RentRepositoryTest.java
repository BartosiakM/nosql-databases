package com.rental.repository;

import com.mongodb.MongoWriteException;
import com.rental.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentRepositoryTest {

    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setUp() {
        rentRepository = new RentRepository();
        clientRepository = new ClientRepository();
        vehicleRepository = new VehicleRepository();
    }

    @AfterEach
    public void cleanup() {
        // Clear the collections after each test
        rentRepository.getDatabase().getCollection("rents",Rent.class).deleteMany(new org.bson.Document());
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new org.bson.Document());
        vehicleRepository.getDatabase().getCollection("vehicles", Vehicle.class).deleteMany(new org.bson.Document());
    }

    @Test
    public void testAddRent() {
        ClientType type = new GoldClientType();
        Client client = new Client(1,"Joe Doe", type);
        Vehicle vehicle = new Car(1,"El7v200", 800, 2, "A");
        Rent rent = new Rent(1,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);
        Rent addedRent = rentRepository.add(rent);

        assertNotNull(addedRent);
        assertEquals(vehicle.getPlateNumber(), addedRent.getVehicle().getPlateNumber());
        assertEquals(1,vehicleRepository.findById(vehicle.getId()).isAvailable());
        assertEquals(client.getUsername(), addedRent.getClient().getUsername());
    }

    @Test
    public void testGetByID() {
        ClientType type = new GoldClientType();
        Client client = new Client(2,"Joe Doe", type);
        Vehicle vehicle = new Car(2,"El7v200", 800, 2, "A");
        Rent rent = new Rent(2,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);
        rentRepository.add(rent);

        Rent foundRent = rentRepository.findById(rent.getId());
        assertNotNull(foundRent);
        assertEquals(vehicle.getPlateNumber(), foundRent.getVehicle().getPlateNumber());
        assertEquals(client.getUsername(), foundRent.getClient().getUsername());
    }

    @Test
    public void testFindAll() {
        ClientType type1 = new GoldClientType();
        Client client1 = new Client(3,"Joe Doe", type1);
        Vehicle vehicle1 = new Car(3,"El7v200", 800, 2, "A");
        Rent rent1 = new Rent(3,client1, vehicle1);

        ClientType type2 = new DiamondClientType();
        Client client2 = new Client(4,"Johnny Doeski", type2);
        Vehicle vehicle2 = new Bicycle(4,"El1234", 600);
        Rent rent2 = new Rent(4,client2, vehicle2);

        clientRepository.add(client1);
        vehicleRepository.add(vehicle1);
        clientRepository.add(client2);
        vehicleRepository.add(vehicle2);
        rentRepository.add(rent1);
        rentRepository.add(rent2);

        List<Rent> rents = rentRepository.findAll();
        assertFalse(rents.isEmpty(), "Rents list should not be empty.");
        assertEquals(2, rents.size());
    }

    @Test
    public void testRemoveRent() {
        ClientType type = new GoldClientType();
        Client client = new Client(5,"Joe Doe", type);
        Vehicle vehicle = new Car(5,"El7v200", 800, 2, "A");
        Rent rent = new Rent(5,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);
        rentRepository.add(rent);
        rentRepository.delete(rent.getId());

        Rent removedRent = rentRepository.findById(rent.getId());
        assertNull(removedRent, "Rent should be removed from the database.");
    }

    @Test
    public void testRentUnavailableVehicle() {
        ClientType type = new GoldClientType();
        Client client = new Client(6,"Joe Doe", type);
        Vehicle vehicle = new Car(6,"El7v200", 800, 2, "A");
        vehicle.setAvailable(1); // Mark vehicle as unavailable
        Rent rent = new Rent(6,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);

        assertThrows(MongoWriteException.class, () -> {
            rentRepository.add(rent);
        });


        assertEquals(clientRepository.findById(client.getId()).getActiveRents(),0);
        assertEquals(vehicleRepository.findById(vehicle.getId()).isAvailable(),1);
    }

    @Test
    public void testClientWithTooManyRents() {
        ClientType type = new DiamondClientType();
        Client client = new Client(7,"Joe Doe", type);
        Vehicle vehicle = new Car(7,"El7v200", 800, 2, "A");
        client.setActiveRents(10); // Exceed the allowed active rentals
        Rent rent = new Rent(7,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);

        assertThrows(MongoWriteException.class, () -> {
            rentRepository.add(rent);
        });

        assertEquals(clientRepository.findById(client.getId()).getActiveRents(),10);
        assertNotEquals(vehicleRepository.findById(vehicle.getId()).isAvailable(),1);

    }

    @Test
    public void testUpdateEndRent() {
        ClientType type = new GoldClientType();
        Client client = new Client(8,"Joe Doe", type);
        Vehicle vehicle = new Car(8,"El7v200", 800, 2, "A");
        Rent rent = new Rent(8,client, vehicle);

        clientRepository.add(client);
        vehicleRepository.add(vehicle);
        Rent addedRent = rentRepository.add(rent);

        addedRent.endRent();

        rentRepository.update(addedRent);

        assertEquals(vehicleRepository.findById(vehicle.getId()).isAvailable(),0);
        assertEquals(clientRepository.findById(client.getId()).getActiveRents(),0);
    }
}
