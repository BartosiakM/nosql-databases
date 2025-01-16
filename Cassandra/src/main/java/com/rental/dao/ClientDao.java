package com.rental.dao;
import com.datastax.oss.driver.api.mapper.annotations.*;
import com.rental.model.Client;

@Dao
public interface ClientDao {

    @StatementAttributes(consistencyLevel = "ONE")
    @Select
    Client findById(String id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    Client add(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Delete
    void delete(Client client);
}

