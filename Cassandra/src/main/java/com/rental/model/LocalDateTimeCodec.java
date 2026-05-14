package com.rental.model;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeCodec implements TypeCodec<LocalDateTime> {

    private final TypeCodec<Instant> innerCodec = TypeCodecs.TIMESTAMP;


    @Override
    public GenericType<LocalDateTime> getJavaType() {
        return GenericType.of(LocalDateTime.class);
    }

    @Override
    public DataType getCqlType() {
        return innerCodec.getCqlType();
    }

    @Override
    public ByteBuffer encode(LocalDateTime value, ProtocolVersion protocolVersion) {
        return value == null ? null : innerCodec.encode(value.toInstant(ZoneOffset.UTC), protocolVersion);
    }

    @Override
    public LocalDateTime decode(ByteBuffer bytes, ProtocolVersion protocolVersion) {
        Instant instant = innerCodec.decode(bytes, protocolVersion);
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public String format(LocalDateTime value) {
        return value == null ? null : innerCodec.format(value.toInstant(ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime parse(String value) {
        Instant instant = innerCodec.parse(value);
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
