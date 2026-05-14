package com.rental.repository;

import com.rental.model.Car;
import com.rental.model.Vehicle;
import org.openjdk.jmh.annotations.*;


@State(Scope.Benchmark)
public class VehicleRepositoryBenchmark {

    private VehicleRepository vehicleRepository;
    private RedisVehicleRepository redisRepo;
    private MongoVehicleRepository mongoRepo;

    @Setup
    public void setup() {
        redisRepo = new RedisVehicleRepository();
        mongoRepo = new MongoVehicleRepository();
        vehicleRepository = new VehicleRepository(redisRepo, mongoRepo);

        Vehicle vehicle = new Car(1, "El7v", 800 , 2 , "A");
        vehicleRepository.add(vehicle);

    }

    @Benchmark
    @Warmup(iterations = 0)
    @Measurement(iterations = 5)
    @Fork(value = 1)
    public void benchmarkCacheHit() {

        Vehicle vehicle = vehicleRepository.findById(1);

    }

    @Benchmark
    @Warmup(iterations = 0)
    @Measurement(iterations = 5)
    @Fork(value = 1)
    public void benchmarkCacheMiss() {
        redisRepo.clearCache();
        Vehicle vehicle = vehicleRepository.findById(1);

    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @TearDown
    public void tearDown() {
        if (redisRepo != null) {
            redisRepo.clearCache();
        }
        if (mongoRepo != null) {
            mongoRepo.getDatabase().getCollection("vehicle", Vehicle.class).drop();
        }
    }
}
