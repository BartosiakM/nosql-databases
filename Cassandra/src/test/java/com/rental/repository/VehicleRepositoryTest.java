package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import com.rental.model.Vehicle;
import com.rental.model.Car;
import com.rental.model.Bicycle;
import com.rental.model.MotorVehicle;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleRepositoryTest {

    private static CqlSession session;
    private static VehicleRepository vehicleRepository;

    @BeforeAll
    public static void setup() {
        CassandraConfig cassandraConfig = new CassandraConfig();
        session = cassandraConfig.getSession();
        vehicleRepository = new VehicleRepository(session);
    }

    @AfterEach
    public void cleanup() {
        Truncate truncate = QueryBuilder.truncate("vehicle");
        session.execute(truncate.build());
    }

    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Car("1", "El7v200", 800, true, 2, "A");

        vehicleRepository.add(vehicle);
        Vehicle retrievedVehicle = vehicleRepository.findById("1");

        assertNotNull(retrievedVehicle);
        assertTrue(retrievedVehicle instanceof Car);
        assertEquals("1", retrievedVehicle.getVehicleId());
        assertEquals("El7v200", retrievedVehicle.getPlateNumber());
        assertEquals(800, retrievedVehicle.getBasePrice());
    }

    @Test
    public void testFindById() {
        Vehicle vehicle = new Bicycle("2", "BIKE123",100,  true);

        vehicleRepository.add(vehicle);

        Vehicle retrievedVehicle = vehicleRepository.findById("2");

        assertNotNull(retrievedVehicle);
        assertTrue(retrievedVehicle instanceof Bicycle);
        assertEquals("2", retrievedVehicle.getVehicleId());
        assertEquals("BIKE123", retrievedVehicle.getPlateNumber());
        assertEquals(100, retrievedVehicle.getBasePrice());
    }

    @Test
    public void testFindByIdNotFound() {
        Vehicle retrievedVehicle = vehicleRepository.findById("999");

        assertNull(retrievedVehicle);
    }

    @Test
    public void testUpdateVehicle() {
        Vehicle vehicle = new MotorVehicle("5", "MOTO123", 1500,2, true);
        vehicleRepository.add(vehicle);

        vehicle.setBasePrice(2000);
        vehicleRepository.update(vehicle);

        Vehicle updatedVehicle = vehicleRepository.findById("5");

        assertNotNull(updatedVehicle);
        assertTrue(updatedVehicle instanceof MotorVehicle);
        assertEquals(2000, updatedVehicle.getBasePrice());
    }

    @Test
    public void testDeleteVehicle() {
        Vehicle vehicle = new Car("6", "DEL123", 1200,true, 2, "D");
        vehicleRepository.add(vehicle);

        vehicleRepository.delete(vehicleRepository.findById("6"));

        Vehicle deletedVehicle = vehicleRepository.findById("6");

        assertNull(deletedVehicle);
    }
}
