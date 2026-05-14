package com.rental.provider;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import com.rental.model.*;

public class VehicleProvider {

    private final CqlSession session;


    public VehicleProvider(MapperContext context, EntityHelper<Bicycle> bicycleHelper,EntityHelper<MotorVehicle> motorVehicleHelper,EntityHelper<Vehicle> vehicleHelper,EntityHelper<Car> carHelper) {
        this.session = context.getSession();
    }

    public Vehicle add(Vehicle vehicle) {
        Insert insert;

        if (vehicle instanceof MotorVehicle) {
            MotorVehicle motorVehicle = (MotorVehicle) vehicle;
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(motorVehicle.getVehicleId()))
                    .value("plate_number", QueryBuilder.literal(motorVehicle.getPlateNumber()))
                    .value("base_price", QueryBuilder.literal(motorVehicle.getBasePrice()))
                    .value("available", QueryBuilder.literal(motorVehicle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("motorvehicle"))
                    .value("engine_displacement", QueryBuilder.literal(motorVehicle.getEngineDisplacement()));
        } else if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(car.getVehicleId()))
                    .value("plate_number", QueryBuilder.literal(car.getPlateNumber()))
                    .value("base_price", QueryBuilder.literal(car.getBasePrice()))
                    .value("available", QueryBuilder.literal(car.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("car"))
                    .value("engine_displacement", QueryBuilder.literal(car.getEngineDisplacement()))
                    .value("segment", QueryBuilder.literal(car.getSegment()));
        } else if (vehicle instanceof Bicycle) {
            Bicycle bicycle = (Bicycle) vehicle;
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(bicycle.getVehicleId()))
                    .value("plate_number", QueryBuilder.literal(bicycle.getPlateNumber()))
                    .value("base_price", QueryBuilder.literal(bicycle.getBasePrice()))
                    .value("available", QueryBuilder.literal(bicycle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("bicycle"));
        } else if (vehicle instanceof Vehicle) {
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(vehicle.getVehicleId()))
                    .value("plate_number", QueryBuilder.literal(vehicle.getPlateNumber()))
                    .value("base_price", QueryBuilder.literal(vehicle.getBasePrice()))
                    .value("available", QueryBuilder.literal(vehicle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("vehicle"));
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getSimpleName());
        }

        session.execute(insert.build());
        return vehicle;
    }

    public Vehicle findById(String vehicleId) {
        Select select = QueryBuilder.selectFrom("vehicle")
                .all()
                .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicleId)));

        ResultSet resultSet = session.execute(select.build());
        Row row = resultSet.one();

        if (row == null) {
            return null;
        }

        String discriminator = row.getString("discriminator");
        if ("motorvehicle".equalsIgnoreCase(discriminator)) {
            return new MotorVehicle(
                    row.getString("vehicle_id"),
                    row.getString("plate_number"),
                    row.getInt("base_price"),
                    row.getInt("engine_displacement"),
                    row.getBoolean("available")
            );
        } else if ("car".equalsIgnoreCase(discriminator)) {
            return new Car(
                    row.getString("vehicle_id"),
                    row.getString("plate_number"),
                    row.getInt("base_price"),
                    row.getBoolean("available"),
                    row.getInt("engine_displacement"),
                    row.getString("segment")
            );
        } else if ("bicycle".equalsIgnoreCase(discriminator)) {
            return new Bicycle(
                    row.getString("vehicle_id"),
                    row.getString("plate_number"),
                    row.getInt("base_price"),
                    row.getBoolean("available")
            );
        } else if ("vehicle".equalsIgnoreCase(discriminator)) {
            return new Vehicle(
                    row.getString("vehicle_id"),
                    row.getString("plate_number"),
                    row.getInt("base_price"),
                    row.getBool("available")
            );
        }

        return null;
    }

    public void update(Vehicle vehicle) {
        Update update;

        if (vehicle instanceof MotorVehicle) {
            MotorVehicle motorVehicle = (MotorVehicle) vehicle;
            update = QueryBuilder.update("vehicle")
                    .setColumn("plate_number", QueryBuilder.literal(motorVehicle.getPlateNumber()))
                    .setColumn("base_price", QueryBuilder.literal(motorVehicle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(motorVehicle.isAvailable()))
                    .setColumn("engine_displacement", QueryBuilder.literal(motorVehicle.getEngineDisplacement()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(motorVehicle.getVehicleId())));
        } else if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            update = QueryBuilder.update("vehicle")
                    .setColumn("plate_number", QueryBuilder.literal(car.getPlateNumber()))
                    .setColumn("base_price", QueryBuilder.literal(car.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(car.isAvailable()))
                    .setColumn("engine_displacement", QueryBuilder.literal(car.getEngineDisplacement()))
                    .setColumn("segment", QueryBuilder.literal(car.getSegment()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(car.getVehicleId())));
        } else if (vehicle instanceof Bicycle) {
            Bicycle bicycle = (Bicycle) vehicle;
            update = QueryBuilder.update("vehicle")
                    .setColumn("plate_number", QueryBuilder.literal(bicycle.getPlateNumber()))
                    .setColumn("base_price", QueryBuilder.literal(bicycle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(bicycle.isAvailable()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(bicycle.getVehicleId())));
        } else if (vehicle instanceof Vehicle) {
            update = QueryBuilder.update("vehicle")
                    .setColumn("plate_number", QueryBuilder.literal(vehicle.getPlateNumber()))
                    .setColumn("base_price", QueryBuilder.literal(vehicle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(vehicle.isAvailable()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicle.getVehicleId())));
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getSimpleName());
        }

        session.execute(update.build());
    }

    public void delete(String vehicleId) {
        Delete delete = QueryBuilder.deleteFrom("vehicle")
                .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicleId)));

        session.execute(delete.build());
    }
}
