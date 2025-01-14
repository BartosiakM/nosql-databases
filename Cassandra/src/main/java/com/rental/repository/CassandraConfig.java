package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.rental.model.ClientTypeCodec;

import java.net.InetSocketAddress;

public class CassandraConfig implements AutoCloseable {
    private static CqlSession session;

    public CassandraConfig() {
        initSession();
    }

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CqlIdentifier.fromCql("car_rental"))
                .build();

        registerCustomCodecs();

        CreateKeyspace keyspace = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql("car_rental"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    private void registerCustomCodecs() {
        MutableCodecRegistry codecRegistry = (MutableCodecRegistry) session.getContext().getCodecRegistry();
        codecRegistry.register(new ClientTypeCodec(codecRegistry.codecFor(String.class)));
    }

    public CqlSession getSession() {
        if (session == null) {
            initSession();
        }
        return session;
    }

    @Override
    public void close(){
        if(session != null) {
            session.close();
        }
    }
}