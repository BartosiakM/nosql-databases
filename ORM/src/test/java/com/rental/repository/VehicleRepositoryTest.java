package com.rental.repository;

import com.rental.model.Vehicle;
import com.rental.model.Car;
import com.rental.model.Bicycle;
import com.rental.model.MotorVehicle;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleRepositoryTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private VehicleRepository vehicleRepository;

    @BeforeAll
    public static void init() {
        emf = Persistence.createEntityManagerFactory("NBDUnit");
    }

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        vehicleRepository = new VehicleRepository();
    }

    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Car("El7v200", 800, 2, "A");
        Vehicle addedVehicle = vehicleRepository.add(vehicle);

        assertNotNull(addedVehicle.getVehicleId());
        assertEquals("El7v200", addedVehicle.getPlateNumber());
        assertEquals(800, addedVehicle.getBasePrice());
    }

    @Test
    public void testGetByID() {
        Vehicle vehicle = new Car("El7v200", 800, 2, "A");
        vehicleRepository.add(vehicle);

        Vehicle foundVehicle = vehicleRepository.getByID(vehicle.getVehicleId());
        assertNotNull(foundVehicle);
        assertEquals("El7v200", foundVehicle.getPlateNumber());
        assertEquals(800, foundVehicle.getBasePrice());
    }

    @Test
    public void testFindAll() {
        Vehicle vehicle1 = new Car("El7v200", 800, 2, "A");
        Vehicle vehicle2 = new Bicycle("EL23412", 50);

        vehicleRepository.add(vehicle1);
        vehicleRepository.add(vehicle2);

        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertFalse(vehicles.isEmpty());
        assertEquals(2, vehicles.size());
    }

    @Test
    public void testRemoveVehicle() {
        Vehicle vehicle = new MotorVehicle("El7v200", 800, 2);
        vehicleRepository.add(vehicle);
        vehicleRepository.remove(vehicle);

        Vehicle removedVehicle = vehicleRepository.getByID(vehicle.getVehicleId());
        assertNull(removedVehicle);
    }

    @Test
    public void testUpdateVehicle() {
        Vehicle vehicle = new MotorVehicle("El7v200", 800, 2);
        vehicleRepository.add(vehicle);
        vehicle.setBasePrice(200);
        vehicleRepository.update(vehicle);

        Vehicle updatedVehicle = vehicleRepository.getByID(vehicle.getVehicleId());
        assertEquals(200, updatedVehicle.getBasePrice());
    }
}
