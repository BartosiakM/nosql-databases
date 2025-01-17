package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import com.rental.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentRepositoryTest {

    private static CqlSession session;
    private static RentRepository rentRepository;

    @BeforeAll
    public static void setup() {
        CassandraConfig cassandraConfig = new CassandraConfig();
        session = cassandraConfig.getSession();
        rentRepository = new RentRepository(session);
        session.execute("TRUNCATE rent_by_client");
        session.execute("TRUNCATE rent_by_vehicle");
    }

    @AfterEach
    public void cleanup() {
        Truncate truncate1 = QueryBuilder.truncate("rent_by_client");
        Truncate truncate2 = QueryBuilder.truncate("rent_by_vehicle");
        session.execute(truncate1.build());
        session.execute(truncate2.build());
    }

    @Test
    public void testAddRent() {
        Client client = new Client("1", "Jane Doe", new DiamondClientType());
        Vehicle vehicle = new Car("1", "El7v300", 900, true,2, "B");
        Rent rent = new Rent("1", client, vehicle);

        rentRepository.add(rent);

        List<Rent> rents = rentRepository.findByClientId("1");
        assertEquals(1, rents.size());
        assertEquals("1", rents.get(0).getClientId());
        assertEquals("1", rents.get(0).getVehicleId());
        assertEquals("1", rents.get(0).getRentId());
    }

    @Test
    public void testFindByClientId() {
        Client client = new Client("1", "Jane Doe", new DiamondClientType());
        Vehicle vehicle1 = new Car("1", "El7v300", 900, true,2, "B");
        Vehicle vehicle2 =  new Bicycle("2","1223", 10, true);
        Rent rent1 = new Rent("1", client, vehicle1);
        Rent rent2 = new Rent("2", client, vehicle2);

        rentRepository.add(rent1);
        rentRepository.add(rent2);

        List<Rent> rents = rentRepository.findByClientId("1");
        assertEquals(2, rents.size());
        assertTrue(rents.stream().anyMatch(r -> r.getRentId().equals("1")));
        assertTrue(rents.stream().anyMatch(r -> r.getRentId().equals("2")));
    }

    @Test
    public void testFindByVehicleId() {
        Client client1 = new Client("1", "Jane Doe", new DiamondClientType());
        Client client2 = new Client("2", "John Doe", new DiamondClientType());
        Vehicle vehicle = new Car("1", "El7v300", 900, true,2, "B");
        Rent rent1 = new Rent("1", client1, vehicle);
        rent1.endRent();
        Rent rent2 = new Rent("2", client2, vehicle);

        rentRepository.add(rent1);
        rentRepository.add(rent2);

        List<Rent> rents = rentRepository.findByVehicleId("1");
        assertEquals(2, rents.size());
        assertTrue(rents.stream().anyMatch(r -> r.getClientId().equals("1")));
        assertTrue(rents.stream().anyMatch(r -> r.getClientId().equals("2")));
    }

    @Test
    public void testDeleteRent() {
        Client client = new Client("1", "Jane Doe", new DiamondClientType());
        Vehicle vehicle = new Car("1", "El7v300", 900, true,2, "B");
        Rent rent = new Rent("1", client, vehicle);

        rentRepository.add(rent);
        assertEquals(1, rentRepository.findByClientId("1").size());

        rentRepository.delete(rent);
        List<Rent> rents = rentRepository.findByClientId("1");
        assertEquals(0,rents.size());
    }

    @Test
    public void testUpdateRent() {
        LocalDateTime testDateTime1 = LocalDateTime.of(2023, Month.APRIL, 15, 10, 30);
        LocalDateTime testDateTime2 = LocalDateTime.of(2023, Month.JANUARY, 17, 10, 30);
        Client client = new Client("1", "Jane Doe", new DiamondClientType());
        Vehicle vehicle = new Car("1", "El7v300", 900, true,2, "B");
        Rent rent = new Rent("1", client, vehicle, testDateTime2, testDateTime1,50.0, true);
        rentRepository.add(rent);

        rent.setRentCost(200);
        rentRepository.update(rent);

        List<Rent> rents = rentRepository.findByClientId("1");
        assertEquals(1, rents.size());
        assertEquals(200, rents.get(0).getRentCost());
    }

    @Test
    public void testRentUnavailableVehicle() {
        Client client = new Client("1", "Jane Doe", new DiamondClientType());
        Vehicle vehicle = new Bicycle("1","123",10,true);
        Rent rent = new Rent("1",client,vehicle);
        Rent rent2 = new Rent("2",client,vehicle);
        rentRepository.add(rent);

        assertThrows(IllegalStateException.class, () -> rentRepository.add(rent));
    }

    @Test
    public void testClientWithTooManyRents() {
        ClientType type = new DefaultClientType();
        Client client = new Client("1", "Jane Doe", type);
        Vehicle vehicle1 = new Car("1", "El7v300", 900, true,2, "B");
        Vehicle vehicle2 =  new Bicycle("2","1223", 10, true);
        Rent rent1 = new Rent("1", client, vehicle1);
        Rent rent2 = new Rent("2", client, vehicle2);

        rentRepository.add(rent1);

        assertThrows(IllegalStateException.class, () -> rentRepository.add(rent2));
    }
}
