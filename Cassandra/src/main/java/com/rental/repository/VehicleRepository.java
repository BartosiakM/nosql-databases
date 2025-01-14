package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.rental.dao.VehicleDao;
import com.rental.mapper.VehicleMapper;
import com.rental.mapper.VehicleMapperBuilder;
import com.rental.model.Vehicle;

public class VehicleRepository extends CassandraConfig implements IRepository<Vehicle> {

    private final CqlSession session;
    private final VehicleMapper vehicleMapper;
    private final VehicleDao vehicleDao;

    public VehicleRepository(CqlSession session) {
        this.session = session;
        createTable();
        this.vehicleMapper = new VehicleMapperBuilder(getSession()).build();
        this.vehicleDao = vehicleMapper.vehicleDao();

    }

    public void createTable() {
        SimpleStatement createVehicles = SchemaBuilder.createTable(CqlIdentifier.fromCql("vehicle"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("vehicle_id"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("base_price"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("available"), DataTypes.BOOLEAN)
                .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("engine_displacement"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("segment"), DataTypes.TEXT)
                .build();

        getSession().execute(createVehicles);
    }

    public void dropTable() {
        SimpleStatement dropVehicles = SchemaBuilder.dropTable(CqlIdentifier.fromCql("vehicle"))
                .ifExists()
                .build();

        getSession().execute(dropVehicles);
    }

    @Override
    public Vehicle add(Vehicle obj) {
        return vehicleDao.add(obj);
    }

    @Override
    public Vehicle findById(long id) {
        return vehicleDao.findById(id);
    }

    @Override
    public void update(Vehicle obj) {
         vehicleDao.update(obj);
    }

    @Override
    public void delete(Vehicle vehicle) {
         vehicleDao.delete(vehicle);
    }
}
