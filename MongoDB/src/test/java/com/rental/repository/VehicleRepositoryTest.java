//package com.rental.repository;
//
//import com.rental.model.Vehicle;
//import com.rental.model.Car;
//import com.rental.model.Bicycle;
//import com.rental.model.MotorVehicle;
//import org.junit.jupiter.api.*;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class VehicleRepositoryTest {
//
//    private VehicleRepository vehicleRepository;
//
//    @BeforeEach
//    public void setup() {
//        vehicleRepository = new VehicleRepository();
//    }
//
//    @AfterEach
//    public void cleanup() {
//        // Czyszczenie kolekcji "vehicles" po każdym teście
//        vehicleRepository.getDatabase().getCollection("vehicles").deleteMany(new org.bson.Document());
//    }
//
//    @Test
//    public void testAddVehicle() {
//        Vehicle vehicle = new Car("El7v200", 800, 2, "A");
//
//        Vehicle result = vehicleRepository.add(vehicle);
//
//        assertNotNull(result);
//        Optional<Vehicle> foundVehicle = vehicleRepository.findById(vehicle.getVehicleId());
//        assertTrue(foundVehicle.isPresent());
//        assertEquals(vehicle.getVehicleId(), foundVehicle.get().getVehicleId());
//        assertEquals("El7v200", foundVehicle.get().getPlateNumber());
//        assertEquals(800, foundVehicle.get().getBasePrice());
//    }
//
//    @Test
//    public void testFindById() {
//        Vehicle vehicle = new Car("El7v200", 800, 2, "A");
//        vehicleRepository.add(vehicle);
//
//        Optional<Vehicle> result = vehicleRepository.findById(vehicle.getVehicleId());
//
//        assertTrue(result.isPresent());
//        assertEquals(vehicle.getVehicleId(), result.get().getVehicleId());
//        assertEquals("El7v200", result.get().getPlateNumber());
//        assertEquals(800, result.get().getBasePrice());
//    }
//
//    @Test
//    public void testFindByIdNotFound() {
//        UUID randomId = UUID.randomUUID();
//
//        Optional<Vehicle> result = vehicleRepository.findById(randomId);
//
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    public void testFindAll() {
//        Vehicle vehicle1 = new Car("El7v200", 800, 2, "A");
//        Vehicle vehicle2 = new Bicycle("EL23412", 50);
//
//        vehicleRepository.add(vehicle1);
//        vehicleRepository.add(vehicle2);
//
//        List<Vehicle> result = vehicleRepository.findAll();
//
//        assertEquals(2, result.size());
//        assertEquals("El7v200", result.get(0).getPlateNumber());
//        assertEquals("EL23412", result.get(1).getPlateNumber());
//    }
//
//    @Test
//    public void testUpdateVehicle() {
//        Vehicle vehicle = new MotorVehicle("El7v200", 800, 2);
//        vehicleRepository.add(vehicle);
//
//        vehicle.setBasePrice(1000);
//        vehicleRepository.update(vehicle);
//
//        Optional<Vehicle> updatedVehicle = vehicleRepository.findById(vehicle.getVehicleId());
//
//        assertTrue(updatedVehicle.isPresent());
//        assertEquals(1000, updatedVehicle.get().getBasePrice());
//    }
//
//    @Test
//    public void testDeleteVehicle() {
//        Vehicle vehicle = new MotorVehicle("El7v200", 800, 2);
//        vehicleRepository.add(vehicle);
//
//        vehicleRepository.delete(vehicle.getVehicleId());
//
//        Optional<Vehicle> deletedVehicle = vehicleRepository.findById(vehicle.getVehicleId());
//
//        assertFalse(deletedVehicle.isPresent());
//    }
//}
