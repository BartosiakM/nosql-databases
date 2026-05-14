package com.rental;

import com.rental.kafka.Producer;
import com.rental.manager.ClientManager;
import com.rental.manager.RentManager;
import com.rental.manager.VehicleManager;
import com.rental.model.*;
import com.rental.repository.ClientRepository;
import com.rental.repository.RentRepository;
import com.rental.repository.VehicleRepository;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RentRepository rentRepository = new RentRepository();
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();

        Client client  = new Client(1L, "John Doe",new BronzeClientType());

        Vehicle vehicle = new Car(1L,"EL 3123C", 300, 2, "A");

        Rent rent = new Rent(1L,client,vehicle);

        ClientManager clientManager = new ClientManager(clientRepository);
        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);
        RentManager rentManager = new RentManager(rentRepository);

        Producer producer = new Producer();

        clientManager.addClient(client);
        vehicleManager.addVehicle(vehicle);
        rentManager.rentVehicle(rent);

        producer.sendRent(rent,"Wypozyczalnia 123");
        Thread.sleep(5000);
    }
}
