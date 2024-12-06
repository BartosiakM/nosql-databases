package com.rental.repository;

import com.rental.model.MotorVehicle;
import com.rental.model.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class VehicleRepositoryTest {

    private VehicleRepository vehicleRepository;
    private RedisVehicleRepository redisVehicleRepository;
    private MongoVehicleRepository mongoVehicleRepository;

    @BeforeEach
    void setUp() {
        redisVehicleRepository = new RedisVehicleRepository();
        mongoVehicleRepository = new MongoVehicleRepository();
        vehicleRepository = new VehicleRepository(redisVehicleRepository, mongoVehicleRepository);
        vehicleRepository.deleteAll();
    }
    @AfterEach
    void tearDown() {
        vehicleRepository.deleteAll();
    }

    @Test
    void testAdd() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);

        Vehicle result = vehicleRepository.add(vehicle);

        Vehicle vehicleRedis = redisVehicleRepository.findById(vehicle.getId());
        Vehicle vehicleMongo = mongoVehicleRepository.findById(vehicle.getId());

        assertNotNull(vehicleRedis);
        assertNotNull(vehicleMongo);
        assertEquals(vehicleRedis.getId(), vehicleMongo.getId());
    }

    @Test
    void testFindById_FoundInRedis() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);
        vehicleRepository.add(vehicle);


        Vehicle result = vehicleRepository.findById(5);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
    }

    @Test
    void testFindById_FoundInMongo() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);


        vehicleRepository.add(vehicle);

        redisVehicleRepository.clearCache();

        Vehicle result = vehicleRepository.findById(5);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
    }

    @Test
    void testUpdate() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);
        vehicleRepository.add(vehicle);

        vehicle.setBasePrice(1400);
        vehicleRepository.update(vehicle);

        Vehicle mongoResult = mongoVehicleRepository.findById(5);
        assertNotNull(mongoResult);
        assertEquals(1400, mongoResult.getBasePrice());


        Vehicle redisResult = redisVehicleRepository.findById(5);
        assertNotNull(redisResult);
        assertEquals(1400, redisResult.getBasePrice());
    }

    @Test
    void testDelete() {
        Vehicle vehicle = new MotorVehicle(5, "El7v200", 800, 2);
        vehicleRepository.add(vehicle);

        vehicleRepository.delete(5);

        assertNull(redisVehicleRepository.findById(5));
        assertNull(mongoVehicleRepository.findById(5));
    }
}
