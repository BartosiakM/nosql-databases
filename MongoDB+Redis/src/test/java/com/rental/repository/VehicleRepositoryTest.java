package com.rental.repository;

import com.rental.model.Vehicle;
import com.rental.model.Car;
import com.rental.model.Bicycle;
import com.rental.model.MotorVehicle;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleRepositoryTest {

    private MongoVehicleRepository vehicleRepository;

    @BeforeEach
    public void setup() {
        vehicleRepository = new MongoVehicleRepository();
    }

    @AfterEach
    public void cleanup() {
        vehicleRepository.getDatabase().getCollection("vehicles").deleteMany(new org.bson.Document());
    }

    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Car(1, "El7v200", 800, 2, "A");

        Vehicle result = vehicleRepository.add(vehicle);

        assertNotNull(result);
        Vehicle foundVehicle = vehicleRepository.findById(vehicle.getId());
        assertEquals(vehicle.getId(), foundVehicle.getId());
        assertEquals("El7v200", foundVehicle.getPlateNumber());
        assertEquals(800, foundVehicle.getBasePrice());
    }

    @Test
    public void testFindById() {
        Vehicle vehicle = new Car(2,"El7v200", 800, 2, "A");
        vehicleRepository.add(vehicle);

        Vehicle result = vehicleRepository.findById(vehicle.getId());


        assertEquals(vehicle.getId(), result.getId());
        assertEquals("El7v200", result.getPlateNumber());
        assertEquals(800, result.getBasePrice());
    }

    @Test
    public void testFindByIdNotFound() {
        long randomId = 11111;

        Vehicle result = vehicleRepository.findById(randomId);

        assertNull(result);
    }

    @Test
    public void testFindAll() {
        Vehicle vehicle1 = new Car(3,"El7v200", 800, 2, "A");
        Vehicle vehicle2 = new Bicycle(4,"EL23412", 50);

        vehicleRepository.add(vehicle1);
        vehicleRepository.add(vehicle2);

        List<Vehicle> result = vehicleRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("El7v200", result.get(0).getPlateNumber());
        assertEquals("EL23412", result.get(1).getPlateNumber());
    }

    @Test
    public void testUpdateVehicle() {
        Vehicle vehicle = new MotorVehicle(5,"El7v200", 800, 2);
        vehicleRepository.add(vehicle);

        vehicle.setBasePrice(1000);
        vehicleRepository.update(vehicle);

        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId());

        assertEquals(1000, updatedVehicle.getBasePrice());
    }

    @Test
    public void testDeleteVehicle() {
        Vehicle vehicle = new MotorVehicle(6,"El7v200", 800, 2);
        vehicleRepository.add(vehicle);

        vehicleRepository.delete(vehicle.getId());

        Vehicle deletedVehicle = vehicleRepository.findById(vehicle.getId());

        assertNull(deletedVehicle);
    }
}
