package com.rental.mapper;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.rental.dao.VehicleDao;

@Mapper
public interface VehicleMapper {
    @DaoFactory
    VehicleDao vehicleDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    VehicleDao vehicleDao();
}