package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.rental.dao.ClientDao;
import com.rental.mapper.ClientMapper;
import com.rental.mapper.ClientMapperBuilder;
import com.rental.model.Client;

public class ClientRepository extends CassandraConfig implements IRepository<Client> {

    private final CqlSession session;
    private final ClientMapper clientMapper;
    private final ClientDao clientDao;;

    public ClientRepository(CqlSession session) {
        this.session = session;
        createTable();
        this.clientMapper = new ClientMapperBuilder(session).build();
        this.clientDao = clientMapper.clientDao();
    }

    public void createTable() {
        SimpleStatement createClients = SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.BIGINT)
                .withClusteringColumn(CqlIdentifier.fromCql("type"), DataTypes.TEXT)
                .withColumn("username", DataTypes.TEXT)
                .withColumn("activeRents", DataTypes.INT)
                .build();
        session.execute(createClients);
    }

    public void dropTable() {
        SimpleStatement dropClients = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients"))
                .ifExists()
                .build();

        getSession().execute(dropClients);
    }

    @Override
    public Client add(Client obj) {
        return clientDao.add(obj);
    }

    @Override
    public Client findById(long id) {
        return clientDao.findById(id);
    }

    @Override
    public void update(Client obj) {
        clientDao.update(obj);
    }

    @Override
    public void delete(Client client) {
        clientDao.delete(client);
    }
}
