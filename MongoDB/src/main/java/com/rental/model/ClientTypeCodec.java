package com.rental.model;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class ClientTypeCodec implements Codec<ClientType> {

    private final CodecRegistry codecRegistry;

    public ClientTypeCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, ClientType clientType, EncoderContext encoderContext) {
        writer.writeStartDocument();
        if (clientType instanceof DefaultClientType) {
            writer.writeString("ClientType", "default");
        } else if (clientType instanceof BronzeClientType) {
            writer.writeString("ClientType", "bronze");
        } else if (clientType instanceof DiamondClientType) {
            writer.writeString("ClientType", "diamond");
        } else if (clientType instanceof GoldClientType) {
            writer.writeString("ClientType", "gold");
        }
        writer.writeEndDocument();
    }

    @Override
    public ClientType decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String clientTypeValue = reader.readString("ClientType");
        ClientType clientType;
        switch (clientTypeValue) {
            case "bronze":
                clientType = new BronzeClientType();
                break;
            case "diamond":
                clientType = new DiamondClientType();
                break;
            case "gold":
                clientType = new GoldClientType();
                break;
            case "default":
            default:
                clientType = new DefaultClientType();
                break;
        }
        reader.readEndDocument();
        return clientType;
    }

    @Override
    public Class<ClientType> getEncoderClass() {
        return ClientType.class;
    }
}
