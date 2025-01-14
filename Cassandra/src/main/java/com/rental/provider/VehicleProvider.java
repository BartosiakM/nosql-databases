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
                    .value("vehicle_id", QueryBuilder.literal(motorVehicle.getId()))
                    .value("plateNumber", QueryBuilder.literal(motorVehicle.getPlateNumber()))
                    .value("basePrice", QueryBuilder.literal(motorVehicle.getBasePrice()))
                    .value("available", QueryBuilder.literal(motorVehicle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("motorvehicle"))
                    .value("engineDisplacement", QueryBuilder.literal(motorVehicle.getEngineDisplacement()));
        } else if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(car.getId()))
                    .value("plateNumber", QueryBuilder.literal(car.getPlateNumber()))
                    .value("basePrice", QueryBuilder.literal(car.getBasePrice()))
                    .value("available", QueryBuilder.literal(car.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("car"))
                    .value("engineDisplacement", QueryBuilder.literal(car.getEngineDisplacement()))
                    .value("segment", QueryBuilder.literal(car.getSegment()));
        } else if (vehicle instanceof Bicycle) {
            Bicycle bicycle = (Bicycle) vehicle;
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(bicycle.getId()))
                    .value("plateNumber", QueryBuilder.literal(bicycle.getPlateNumber()))
                    .value("basePrice", QueryBuilder.literal(bicycle.getBasePrice()))
                    .value("available", QueryBuilder.literal(bicycle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("bicycle"));
        } else if (vehicle instanceof Vehicle) {
            insert = QueryBuilder.insertInto("vehicle")
                    .value("vehicle_id", QueryBuilder.literal(vehicle.getId()))
                    .value("plateNumber", QueryBuilder.literal(vehicle.getPlateNumber()))
                    .value("basePrice", QueryBuilder.literal(vehicle.getBasePrice()))
                    .value("available", QueryBuilder.literal(vehicle.isAvailable()))
                    .value("discriminator", QueryBuilder.literal("vehicle"));
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getSimpleName());
        }

        session.execute(insert.build());
        return vehicle;
    }

    public Vehicle findById(long vehicleId) {
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
                    row.getLong("vehicle_id"),
                    row.getString("plateNumber"),
                    row.getInt("basePrice"),
                    row.getInt("engineDisplacement"),
                    row.getBoolean("available")
            );
        } else if ("car".equalsIgnoreCase(discriminator)) {
            return new Car(
                    row.getLong("vehicle_id"),
                    row.getString("plateNumber"),
                    row.getInt("basePrice"),
                    row.getBoolean("available"),
                    row.getInt("engineDisplacement"),
                    row.getString("segment")
            );
        } else if ("bicycle".equalsIgnoreCase(discriminator)) {
            return new Bicycle(
                    row.getLong("vehicle_id"),
                    row.getString("plateNumber"),
                    row.getInt("basePrice"),
                    row.getBoolean("available")
            );
        } else if ("vehicle".equalsIgnoreCase(discriminator)) {
            return new Vehicle(
                    row.getLong("vehicle_id"),
                    row.getString("plateNumber"),
                    row.getInt("basePrice"),
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
                    .setColumn("plateNumber", QueryBuilder.literal(motorVehicle.getPlateNumber()))
                    .setColumn("basePrice", QueryBuilder.literal(motorVehicle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(motorVehicle.isAvailable()))
                    .setColumn("engineDisplacement", QueryBuilder.literal(motorVehicle.getEngineDisplacement()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(motorVehicle.getId())));
        } else if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            update = QueryBuilder.update("vehicle")
                    .setColumn("plateNumber", QueryBuilder.literal(car.getPlateNumber()))
                    .setColumn("basePrice", QueryBuilder.literal(car.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(car.isAvailable()))
                    .setColumn("engineDisplacement", QueryBuilder.literal(car.getEngineDisplacement()))
                    .setColumn("segment", QueryBuilder.literal(car.getSegment()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(car.getId())));
        } else if (vehicle instanceof Bicycle) {
            Bicycle bicycle = (Bicycle) vehicle;
            update = QueryBuilder.update("vehicle")
                    .setColumn("plateNumber", QueryBuilder.literal(bicycle.getPlateNumber()))
                    .setColumn("basePrice", QueryBuilder.literal(bicycle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(bicycle.isAvailable()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(bicycle.getId())));
        } else if (vehicle instanceof Vehicle) {
            update = QueryBuilder.update("vehicle")
                    .setColumn("plateNumber", QueryBuilder.literal(vehicle.getPlateNumber()))
                    .setColumn("basePrice", QueryBuilder.literal(vehicle.getBasePrice()))
                    .setColumn("available", QueryBuilder.literal(vehicle.isAvailable()))
                    .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicle.getId())));
        } else {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getSimpleName());
        }

        session.execute(update.build());
    }

    public void delete(long vehicleId) {
        Delete delete = QueryBuilder.deleteFrom("vehicle")
                .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicleId)));

        session.execute(delete.build());
    }
}
