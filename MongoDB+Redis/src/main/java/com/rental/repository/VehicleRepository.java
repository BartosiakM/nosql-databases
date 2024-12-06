package com.rental.repository;

import com.rental.model.Vehicle;

import java.util.List;

public class VehicleRepository  implements IRepository{

    private final RedisVehicleRepository redisRepository;
    private final MongoVehicleRepository mongoRepository;

    public VehicleRepository(RedisVehicleRepository redisRepository, MongoVehicleRepository mongoRepository) {
        this.redisRepository = redisRepository;
        this.mongoRepository = mongoRepository;
    }

    public Vehicle add(Vehicle vehicle) {
        Vehicle addedVehicle = mongoRepository.add(vehicle);
        redisRepository.add(vehicle);
        return addedVehicle;
    }

    public Vehicle findById(long id) {
        Vehicle vehicle = redisRepository.findById(id);
        if (vehicle != null) {
            return vehicle;
        }
        vehicle = mongoRepository.findById(id);
        if (vehicle != null) {
            redisRepository.add(vehicle);
        }
        return vehicle;
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = mongoRepository.findAll();
        for (Vehicle vehicle : vehicles) {
           Vehicle redisVehicle = redisRepository.findById(vehicle.getId());
           if (redisVehicle == null) {
               redisRepository.add(vehicle);
           }
        }
        return vehicles;
    }

    public void update(Vehicle vehicle) {
        mongoRepository.update(vehicle);
        redisRepository.update(vehicle);
    }

    public void delete(long id) {
        mongoRepository.delete(id);
        redisRepository.delete(id);
    }

    public void deleteAll() {
        mongoRepository.getDatabase().getCollection("vehicles").deleteMany(new org.bson.Document());
        redisRepository.clearCache();
    }
}
