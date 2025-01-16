package com.rental.provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import com.rental.model.LocalDateTimeCodec;
import com.rental.model.Rent;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RentProvider {
    private final CqlSession session;
    private final LocalDateTimeCodec timeCodec = new LocalDateTimeCodec();

    public static final CqlIdentifier RENTS_BY_CLIENT = CqlIdentifier.fromCql("rents_by_client");
    public static final CqlIdentifier RENTS_BY_VEHICLE = CqlIdentifier.fromCql("rents_by_vehicle");

    public RentProvider(MapperContext context) {
        this.session = context.getSession();
    }

    public Rent add(Rent rent) {

        Insert insertClient = QueryBuilder.insertInto(RENTS_BY_CLIENT)
                .value("rent_id", QueryBuilder.literal(rent.getRentId()))
                .value("client_id", QueryBuilder.literal(rent.getClientId()))
                .value("vehicle_id", QueryBuilder.literal(rent.getVehicleId()))
                .value("begin_time", QueryBuilder.literal(rent.getBeginTime(), timeCodec))
                .value("end_time", QueryBuilder.literal(rent.getEndTime(), timeCodec))
                .value("rent_cost", QueryBuilder.literal(rent.getRentCost()))
                .value("archive", QueryBuilder.literal(rent.isArchive()))
                .ifNotExists();

        Insert insertVehicle = QueryBuilder.insertInto(RENTS_BY_VEHICLE)
                .value("rent_id", QueryBuilder.literal(rent.getRentId()))
                .value("personal_id", QueryBuilder.literal(rent.getClientId()))
                .value("plate_number", QueryBuilder.literal(rent.getVehicleId()))
                .value("begin_time", QueryBuilder.literal(rent.getBeginTime(), timeCodec))
                .value("end_time", QueryBuilder.literal(rent.getEndTime(), timeCodec))
                .value("rent_cost", QueryBuilder.literal(rent.getRentCost()))
                .value("archived", QueryBuilder.literal(rent.isArchive()))
                .ifNotExists();

        session.execute(insertClient.build());
        session.execute(insertVehicle.build());

        return rent;
    }

    public List<Rent> findByClientId(String clientId) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_CLIENT).all()
                .where(Relation.column("client_id").isEqualTo(QueryBuilder.literal(clientId)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();
        return mapResultSetToRents(rows);
    }

    public List<Rent> findByVehicleId(String vehicleId) {
        Select select = QueryBuilder.selectFrom(RENTS_BY_VEHICLE).all()
                .where(Relation.column("vehicle_id").isEqualTo(QueryBuilder.literal(vehicleId)));
        ResultSet resultSet = session.execute(select.build());
        List<Row> rows = resultSet.all();
        return mapResultSetToRents(rows);
    }

    public void update(Rent rent) {
        Update updateClient = QueryBuilder.update(RENTS_BY_CLIENT)
                .setColumn("begin_time", QueryBuilder.literal(rent.getBeginTime(), timeCodec))
                .setColumn("end_time", QueryBuilder.literal(rent.getEndTime(), timeCodec))
                .setColumn("rent_cost", QueryBuilder.literal(rent.getRentCost()))
                .setColumn("archived", QueryBuilder.literal(rent.isArchive()))
                .where(Relation.column("rent_id").isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column("client_id").isEqualTo(QueryBuilder.literal(rent.getClientId())));

        Update updateVehicle = QueryBuilder.update(RENTS_BY_VEHICLE)
                .setColumn("begin_time", QueryBuilder.literal(rent.getBeginTime(), timeCodec))
                .setColumn("end_time", QueryBuilder.literal(rent.getEndTime(), timeCodec))
                .setColumn("rent_cost", QueryBuilder.literal(rent.getRentCost()))
                .setColumn("archive", QueryBuilder.literal(rent.isArchive()))
                .where(Relation.column("rent_id").isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column("plate_number").isEqualTo(QueryBuilder.literal(rent.getVehicleId())));

        BatchStatement batch = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateClient.build())
                .addStatement(updateVehicle.build())
                .build();

        session.execute(batch);
    }

    public void delete(Rent rent) {
        Delete deleteClient = QueryBuilder.deleteFrom(RENTS_BY_CLIENT)
                .where(Relation.column("rent_id").isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column("client_id").isEqualTo(QueryBuilder.literal(rent.getClientId())));

        Delete deleteVehicle = QueryBuilder.deleteFrom(RENTS_BY_VEHICLE)
                .where(Relation.column("rent_id").isEqualTo(QueryBuilder.literal(rent.getRentId())))
                .where(Relation.column("vehicleId").isEqualTo(QueryBuilder.literal(rent.getVehicleId())));

        BatchStatement batch = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteClient.build())
                .addStatement(deleteVehicle.build())
                .build();

        session.execute(batch);
    }

    private List<Rent> mapResultSetToRents(List<Row> rows) {
        ArrayList<Rent> rents = new ArrayList<>();

        for (Row row : rows) {
            LocalDateTime beginTime = row.isNull(CqlIdentifier.fromCql("begin_time")) ? null : LocalDateTime.ofInstant(row.getInstant(CqlIdentifier.fromCql("begin_time")), ZoneOffset.UTC);
            LocalDateTime endTime = row.isNull(CqlIdentifier.fromCql("end_time")) ? null : LocalDateTime.ofInstant(row.getInstant(CqlIdentifier.fromCql("end_time")), ZoneOffset.UTC);

            Rent rent = new Rent(
                    row.getString(CqlIdentifier.fromCql("rent_id")),
                    row.getString(CqlIdentifier.fromCql("client_id")),
                    row.getString(CqlIdentifier.fromCql("vehicle_id")),
                    beginTime,
                    endTime,
                    row.getDouble(CqlIdentifier.fromCql("rent_cost")),
                    row.getBoolean(CqlIdentifier.fromCql("archived"))
            );
            rents.add(rent);
        }
        return rents;
    }
}
