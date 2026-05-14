package com.rental.model;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class VehicleCodec implements Codec<Vehicle> {

    private final CodecRegistry codecRegistry;

    public VehicleCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, Vehicle vehicle, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeInt64("_id", vehicle.getId());
        writer.writeString("vehicleType", vehicle.getClass().getSimpleName().toLowerCase());
        writer.writeString("plateNumber", vehicle.getPlateNumber());
        writer.writeInt32("basePrice", vehicle.getBasePrice());
        writer.writeInt32("available", vehicle.isAvailable());

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
        long id = reader.readInt64("_id");
        String vehicleType = reader.readString("vehicleType");
        String plateNumber = reader.readString("plateNumber");
        int basePrice = reader.readInt32("basePrice");
        int available = reader.readInt32("available");

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
