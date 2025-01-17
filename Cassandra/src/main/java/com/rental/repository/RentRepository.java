package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.rental.dao.RentDao;
import com.rental.mapper.ClientMapperBuilder;
import com.rental.mapper.RentMapper;
import com.rental.mapper.RentMapperBuilder;
import com.rental.model.Rent;

import java.util.List;

public class RentRepository extends CassandraConfig {

    private final CqlSession session;
    private final RentMapper rentMapper;
    private final RentDao rentDao;

    public RentRepository(CqlSession session) {
        this.session = session;
        createTable();
        this.rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.clientDao();
    }

    public void createTable() {

        SimpleStatement createRentsByClient = SchemaBuilder.createTable(CqlIdentifier.fromCql("rent_by_client"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("vehicle_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("begin_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql("archive"), DataTypes.BOOLEAN)
                .build();

        SimpleStatement createRentsByVehicle = SchemaBuilder.createTable(CqlIdentifier.fromCql("rent_by_vehicle"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("vehicle_id"), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("client_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("begin_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql("archive"), DataTypes.BOOLEAN)
                .build();

        getSession().execute(createRentsByClient);
        getSession().execute(createRentsByVehicle);

    }
    public void dropTable() {
        SimpleStatement dropRentByClients = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rent_by_client"))
                .ifExists()
                .build();

        SimpleStatement dropRentByVehicles = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rent_by_vehicle"))
                .ifExists()
                .build();

        session.execute(dropRentByClients);
        session.execute(dropRentByVehicles);
    }

    public List<Rent> findByClientId(String id){
        return rentDao.findByClientId(id);
    }

    public List<Rent> findByVehicleId(String id){
        return rentDao.findByVehicleId(id);
    }

    public Rent add(Rent obj) {
        return rentDao.add(obj);
    }

    public void update(Rent obj) {
        rentDao.update(obj);
    }

    public void delete(Rent obj) {
         rentDao.delete(obj);
    }
}
