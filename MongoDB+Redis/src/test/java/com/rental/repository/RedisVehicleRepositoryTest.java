package com.rental.repository;

import com.rental.model.Bicycle;
import com.rental.model.Car;
import com.rental.model.MotorVehicle;
import com.rental.model.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedisVehicleRepositoryTest {

    RedisVehicleRepository repo;

    @BeforeEach
    public void setup() {
        repo = new RedisVehicleRepository();
    }

    @AfterEach
    public void teardown() {
        repo.clearCache();
    }

    @Test
    void testAddVehicle() {
        Vehicle vehicle = new Car(1, "El7v200", 800, 2, "A");

        Vehicle result = repo.add(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals("El7v200", result.getPlateNumber());
        assertEquals(800, result.getBasePrice());
    }

    @Test
    void testFindById() {
        Vehicle vehicle = new Car(1, "El7v200", 800, 2, "A");

        repo.add(vehicle);

        Vehicle result = repo.findById(1);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals("El7v200", result.getPlateNumber());
        assertEquals(800, result.getBasePrice());
    }

    @Test
    void testFindByIdNotFound() {
        Vehicle result = repo.findById(1L);

        assertNull(result);
    }

    @Test
    void testFindAll() {
        Vehicle vehicle1 = new Car(3, "El7v200", 800, 2, "A");
        Vehicle vehicle2 = new Bicycle(4, "EL23412", 50);

        repo.add(vehicle1);
        repo.add(vehicle2);

        List<Vehicle> result = repo.findAll();

        assertEquals(2, result.size());
        assertEquals("El7v200", result.get(0).getPlateNumber());
        assertEquals("EL23412", result.get(1).getPlateNumber());
    }

    @Test
    void testUpdateVehicle() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);

        repo.add(vehicle);

        vehicle.setBasePrice(1000);
        repo.update(vehicle);

        Vehicle updatedVehicle = repo.findById(vehicle.getId());

        assertEquals(1000, updatedVehicle.getBasePrice());
    }

    @Test
    void testDeleteVehicle() {
        Vehicle vehicle = new Car(7, "KJ12345", 700, 4, "B");

        repo.add(vehicle);

        repo.delete(vehicle.getId());

        Vehicle result = repo.findById(vehicle.getId());

        assertNull(result);
    }

}
