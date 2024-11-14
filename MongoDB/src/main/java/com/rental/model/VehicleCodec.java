package com.rental.model;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import java.util.UUID;

public class VehicleCodec implements Codec<Vehicle> {

    private final CodecRegistry codecRegistry;

    public VehicleCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, Vehicle vehicle, EncoderContext encoderContext) {
        writer.writeStartDocument();

        // Pole wspólne dla wszystkich typów pojazdów
        writer.writeString("vehicleType", vehicle.getClass().getSimpleName().toLowerCase());
        writer.writeString("_id", vehicle.getVehicleId().toString());
        writer.writeString("plateNumber", vehicle.getPlateNumber());
        writer.writeInt32("basePrice", vehicle.getBasePrice());
        writer.writeBoolean("available", vehicle.isAvailable());

        // Pola specyficzne dla podklas
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            writer.writeInt32("engineDisplacement", car.getEngineDisplacement());
            writer.writeString("segment", car.getSegment());
        } else if (vehicle instanceof MotorVehicle) {
            MotorVehicle motorVehicle = (MotorVehicle) vehicle;
            writer.writeInt32("engineDisplacement", motorVehicle.getEngineDisplacement());
        }

        writer.writeEndDocument();
    }

    @Override
    public Vehicle decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String vehicleType = reader.readString("vehicleType");

        UUID id = UUID.fromString(reader.readString("_id"));
        String plateNumber = reader.readString("plateNumber");
        int basePrice = reader.readInt32("basePrice");
        boolean available = reader.readBoolean("available");

        Vehicle vehicle;
        switch (vehicleType) {
            case "car":
                int engineDisplacementCar = reader.readInt32("engineDisplacement");
                String segment = reader.readString("segment");
                vehicle = new Car(id, plateNumber, basePrice, engineDisplacementCar, segment);
                break;
            case "motorvehicle":
                int engineDisplacementMotor = reader.readInt32("engineDisplacement");
                vehicle = new MotorVehicle(id, plateNumber, basePrice, engineDisplacementMotor);
                break;
            case "bicycle":
            default:
                vehicle = new Bicycle(id, plateNumber, basePrice);
                break;
        }

        vehicle.setAvailable(available);
        reader.readEndDocument();
        return vehicle;
    }

    @Override
    public Class<Vehicle> getEncoderClass() {
        return Vehicle.class;
    }
}
