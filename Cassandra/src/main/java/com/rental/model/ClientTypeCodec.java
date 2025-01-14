package com.rental.model;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;


public class ClientTypeCodec extends MappingCodec<String, ClientType> {

    public ClientTypeCodec(TypeCodec<String> innerCodec) {
        super(innerCodec, GenericType.of(ClientType.class));
    }

    @Override
    protected ClientType innerToOuter(String value) {
        if (value == null) {
            return null;
        }
        switch (value.toUpperCase()) {
            case "BRONZE":
                return new BronzeClientType();
            case "DEFAULT":
                return new DefaultClientType();
            case "GOLD":
                return new GoldClientType();
            case "DIAMOND":
                return new DiamondClientType();
        }
        return new DefaultClientType();
    }

    @Override
    protected String outerToInner(ClientType value) {
        if (value == null) {
            return null;
        }
        return value.getClass().getSimpleName().toUpperCase();
    }
}
