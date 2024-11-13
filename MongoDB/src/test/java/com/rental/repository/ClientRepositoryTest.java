package com.rental.repository;

import com.rental.model.Client;
import com.rental.model.ClientType;
import com.rental.model.DiamondClientType;
import com.rental.model.GoldClientType;
import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private ClientRepository clientRepository;

    @BeforeAll
    public static void init() {
        emf = Persistence.createEntityManagerFactory("NBDUnit");
    }

    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        clientRepository = new ClientRepository();
    }

    @Test
    public void testAddClient() {
        ClientType type = new GoldClientType();
        Client client = new Client("Joe Doe", type);

        Client addedClient = clientRepository.add(client);

        assert(addedClient.getClientId() != null);
        assertEquals("Joe Doe", addedClient.getUsername());
    }

    @Test
    public void testGetByID() {
        ClientType type = new GoldClientType();
        Client client = new Client("Joe Doe", type);

        clientRepository.add(client);

        Client foundClient = clientRepository.getByID(client.getClientId());
        assertNotNull(foundClient);
        assertEquals("Joe Doe", foundClient.getUsername());
    }

    @Test
    public void testFindAll() {
        ClientType type1 = new GoldClientType();
        ClientType type2 = new DiamondClientType();
        Client client1 = new Client("Joe Doe", type1);
        Client client2 = new Client("John Doeski", type2);

        clientRepository.add(client1);
        clientRepository.add(client2);

        List<Client> clients = clientRepository.findAll();
        assertFalse(clients.isEmpty(), "Clients list should not be empty.");
        assertEquals(2, clients.size());
    }

    @Test
    public void testRemoveClient() {
        ClientType type = new GoldClientType();
        Client client = new Client("Joe Doe", type);

        clientRepository.add(client);
        clientRepository.remove(client);

        Client removedClient = clientRepository.getByID(client.getClientId());
        assertNull(removedClient, "Client should be removed from the database.");
    }

    @Test
    public void testUpdateClient() {
        ClientType type = new GoldClientType();
        Client client = new Client("Joe Doe", type);

        clientRepository.add(client);
        client.setUsername("Johnny");
        clientRepository.update(client);

        Client updatedClient = clientRepository.getByID(client.getClientId());
        assertEquals("Johnny", updatedClient.getUsername());
    }
}
