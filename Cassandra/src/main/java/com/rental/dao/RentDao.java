package com.rental.dao;
import com.datastax.oss.driver.api.mapper.annotations.*;
import com.rental.model.Rent;
import com.rental.model.Vehicle;
import com.rental.provider.RentProvider;

import java.util.List;


@Dao
public interface RentDao {
    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByClientId(String id);

    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByVehicleId(String id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    Rent add(Rent vehicle);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void update(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void delete(Rent rent);
}