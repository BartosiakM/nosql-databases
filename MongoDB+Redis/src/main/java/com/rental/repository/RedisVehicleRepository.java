package com.rental.repository;

import com.rental.model.Vehicle;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.ArrayList;
import java.util.List;

public class RedisVehicleRepository extends AbstractRedisRepository implements IRepository {

    private static final String VEHICLE_KEY_PREFIX = "vehicle:";
    private static final Jsonb jsonb = JsonbBuilder.create();

    public RedisVehicleRepository() {
        this.initDbConnection();
    }

    @Override
    public Vehicle add(Vehicle vehicle) {
        try {
            String key = VEHICLE_KEY_PREFIX + vehicle.getId();

            String json = jsonb.toJson(vehicle);

            pool.set(key, json);

            pool.expire(key, 3600);

            return vehicle;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add vehicle to Redis", e);
        }
    }

    @Override
    public Vehicle findById(long id) {
        try {
            String key = VEHICLE_KEY_PREFIX + id;

            String json = pool.get(key);
            if (json == null) {
                return null;
            }
            return jsonb.fromJson(json, Vehicle.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to find vehicle in Redis", e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        try {
            List<Vehicle> vehicles = new ArrayList<>();

            for (String key : pool.keys(VEHICLE_KEY_PREFIX + "*")) {
                String json = pool.get(key);
                Vehicle vehicle = jsonb.fromJson(json, Vehicle.class);
                vehicles.add(vehicle);
            }

            return vehicles;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch all vehicles from Redis", e);
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        try {
            String key = VEHICLE_KEY_PREFIX + vehicle.getId();
            if (!pool.exists(key)) {
                throw new RuntimeException("Vehicle does not exist in Redis");
            }

            String json = jsonb.toJson(vehicle);
            pool.set(key, json);

            pool.expire(key, 3600);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update vehicle in Redis", e);
        }
    }

    @Override
    public void delete(long id) {
        try {
            String key = VEHICLE_KEY_PREFIX + id;
            if (pool.exists(key)) {
                pool.del(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete vehicle from Redis", e);
        }
    }
}
