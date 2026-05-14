package com.rental.repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.truncate.Truncate;
import com.rental.model.Client;
import com.rental.model.DefaultClientType;
import com.rental.model.GoldClientType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private static CqlSession session;
    private static ClientRepository clientRepository;

    @BeforeAll
    public static void setup() {
        CassandraConfig cassandraConfig = new CassandraConfig();
        session = cassandraConfig.getSession();
        clientRepository = new ClientRepository(session);
    }

    @AfterEach
    public void cleanup() {
        Truncate truncate = QueryBuilder.truncate("clients");
        session.execute(truncate.build());
    }

    @Test
    public void testAddClient() {
        Client client = new Client("1", "testUser",new DefaultClientType());
        client.setActiveRents(3);

        clientRepository.add(client);
        Client retrievedClient = clientRepository.findById("1");

        assertNotNull(retrievedClient);
        assertEquals("1", retrievedClient.getClientId());
        assertEquals("testUser", retrievedClient.getUsername());
        assertEquals(3, retrievedClient.getActiveRents());
    }

    @Test
    public void testFindById() {
        Client client = new Client("2", "anotherUser", new DefaultClientType());
        client.setActiveRents(5);
        clientRepository.add(client);

        Client retrievedClient = clientRepository.findById("2");

        assertNotNull(retrievedClient);
        assertEquals("2", retrievedClient.getClientId());
        assertEquals("anotherUser", retrievedClient.getUsername());
        assertEquals(5, retrievedClient.getActiveRents());
    }

    @Test
    public void testFindByIdNotFound() {
        Client retrievedClient = clientRepository.findById("999");

        assertNull(retrievedClient);
    }

    @Test
    public void testUpdateClient() {
        Client client = new Client("3", "toBeUpdated", new DefaultClientType());
        client.setActiveRents(2);
        clientRepository.add(client);

        // When
        Client updatedClient = new Client("3", "updatedUser", new GoldClientType());
        updatedClient.setActiveRents(10);
        clientRepository.update(updatedClient);

        Client retrievedClient = clientRepository.findById("3");

        // Then
        assertNotNull(retrievedClient);
        assertEquals("3", retrievedClient.getClientId());
        assertEquals("updatedUser", retrievedClient.getUsername());
        assertEquals(10, retrievedClient.getActiveRents());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client("4", "toBeDeleted", new DefaultClientType());
        client.setActiveRents(1);
        clientRepository.add(client);

        clientRepository.delete(client);
        Client retrievedClient = clientRepository.findById("4");

        assertNull(retrievedClient);
    }
}
