//package com.rental.repository;
//
//import com.mongodb.client.ClientSession;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Updates;
//import com.rental.model.Client;
//import com.rental.model.Rent;
//import com.rental.model.Vehicle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.mongodb.client.model.Filters.eq;
//
//public class RentRepository extends CassandraConfig {
//
//    private final MongoCollection<Rent> rentCollection;
//    private final MongoCollection<Client> clientCollection;
//    private final MongoCollection<Vehicle> vehicleCollection;
//
//
//    public RentRepository() {
//        this.rentCollection = getDatabase().getCollection("rents", Rent.class);
//        this.clientCollection = getDatabase().getCollection("clients", Client.class);
//        this.vehicleCollection = getDatabase().getCollection("vehicles", Vehicle.class);
//    }
//
//    public Rent findById(long id) {
//        return rentCollection.find(eq("_id", id)).first();
//    }
//
//    public List<Rent> findAll() {
//        List<Rent> rents = new ArrayList<>();
//        rentCollection.find().into(rents);
//        return rents;
//    }
//
//    public Rent add(Rent rent) {
//        try (ClientSession session = getMongoClient().startSession()) {
//            session.startTransaction();
//
//            try {
//                rent.getClient().setActiveRents(rent.getClient().getActiveRents() + 1);
//                rentCollection.insertOne(session, rent);
//
//                clientCollection.updateOne(
//                        session,
//                        Filters.eq("_id", rent.getClient().getId()),
//                        Updates.inc("activeRents", 1)
//                );
//
//                vehicleCollection.updateOne(
//                        session,
//                        Filters.eq("_id", rent.getVehicle().getId()),
//                        Updates.inc("available", 1)
//                );
//
//                session.commitTransaction();
//                return rent;
//
//            } catch (Exception e) {
//                session.abortTransaction();
//                throw e;
//            } finally {
//                session.close();
//            }
//        }
//    }
//
//    public void update(Rent rent) {
//        if (rent == null) {
//            throw new IllegalArgumentException("Rent or rent ID cannot be null");
//        }
//
//        try (ClientSession session = getMongoClient().startSession()) {
//            session.startTransaction();
//
//            try {
//                rentCollection.updateOne(
//                        session,
//                        eq("_id", rent.getId()),
//                        Updates.combine(
//                                Updates.set("client",rent.getClient()),
//                                Updates.set("vehicle",rent.getVehicle()),
//                                Updates.set("begineTime", rent.getBeginTime()),
//                                Updates.set("endTime", rent.getEndTime()),
//                                Updates.set("rentCost", rent.getRentCost()),
//                                Updates.set("archive", rent.isArchive())
//                        )
//                );
//
//                if (rent.getEndTime() != null) {
//                    clientCollection.updateOne(
//                            session,
//                            Filters.eq("_id", rent.getClient().getId()),
//                            Updates.inc("activeRents", -1)
//                    );
//
//                    vehicleCollection.updateOne(
//                            session,
//                            Filters.eq("_id", rent.getVehicle().getId()),
//                            Updates.inc("available", -1)
//                    );
//                }
//
//                session.commitTransaction();
//            } catch (Exception e) {
//                session.abortTransaction();
//                System.err.println("Transaction aborted due to error: " + e.getMessage());
//                throw new RuntimeException("Failed to update rent transactionally", e);
//            } finally {
//                session.close();
//            }
//        }
//    }
//
//
//    public void delete(long  id) {
//        rentCollection.deleteOne(eq("_id", id));
//    }
//
//}