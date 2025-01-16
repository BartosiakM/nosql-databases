package com.rental.model;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import java.nio.ByteBuffer;

public class ClientTypeCodec implements TypeCodec<ClientType> {

    private static final GenericType<ClientType> JAVA_TYPE = GenericType.of(ClientType.class);

    @Override
    public GenericType<ClientType> getJavaType() {
        return JAVA_TYPE;
    }

    @Override
    public DataType getCqlType() {
        return com.datastax.oss.driver.api.core.type.DataTypes.TEXT; // ClientType stored as TEXT in Cassandra
    }

    @Override
    public ByteBuffer encode(ClientType value, ProtocolVersion protocolVersion) {
        if (value == null) {
            return null;
        }
        String className = value.getClass().getSimpleName().toUpperCase();
        return ByteBuffer.wrap(className.getBytes());
    }

    @Override
    public ClientType decode(ByteBuffer bytes, ProtocolVersion protocolVersion) {
        if (bytes == null || !bytes.hasRemaining()) {
            return null;
        }
        String className = new String(bytes.array());
        return switch (className.toUpperCase()) {
            case "DEFAULTCLIENTTYPE" -> new DefaultClientType();
            case "BRONZECLIENTTYPE" -> new BronzeClientType();
            case "GOLDCLIENTTYPE" -> new GoldClientType();
            case "DIAMONDCLIENTTYPE" -> new DiamondClientType();
            default -> throw new IllegalArgumentException("Unknown ClientType: " + className);
        };
    }

    @Override
    public String format(ClientType value) {
        if (value == null) {
            return "NULL";
        }
        return "'" + value.getClass().getSimpleName().toUpperCase() + "'";
    }

    @Override
    public ClientType parse(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        }
        return switch (value.replace("'", "").toUpperCase()) {
            case "DEFAULTCLIENTTYPE" -> new DefaultClientType();
            case "BRONZECLIENTTYPE" -> new BronzeClientType();
            case "GOLDCLIENTTYPE" -> new GoldClientType();
            case "DIAMONDCLIENTTYPE" -> new DiamondClientType();
            default -> throw new IllegalArgumentException("Unknown ClientType: " + value);
        };
    }
}
