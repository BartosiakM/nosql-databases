package com.rental.repository;

import com.rental.model.*;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        // Inicjalizacja ClientRepository przed każdym testem
        clientRepository = new ClientRepository();
    }

    @AfterEach
    public void cleanup() {
        // Czyszczenie kolekcji po każdym teście
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new org.bson.Document());
    }

    @Test
    public void testAddClient() {
        ClientType type = new BronzeClientType();
        Client client = new Client(123, "JohnDoe", type);

        Client result = clientRepository.add(client);

        assertNotNull(result);
        System.out.println(result.getId());
        Client foundClient = clientRepository.findById(client.getId());
        assertEquals(client.getId(), foundClient.getId());
    }

    @Test
    public void testFindById() {
        ClientType type = new BronzeClientType();
        Client client = new Client(1, "JaneDoe", type);
        clientRepository.add(client);

        Client result = clientRepository.findById(client.getId());

        assertEquals(client.getUsername(), result.getUsername());
    }

    @Test
    public void testFindByIdNotFound() {
        long randomId = 11111111;

        Client result = clientRepository.findById(randomId);

        assertNull(result);
    }

    @Test
    public void testFindAll() {
        Client client1 = new Client(2, "Alice", new BronzeClientType());
        Client client2 = new Client(3, "Bob", new DefaultClientType());

        clientRepository.add(client1);
        clientRepository.add(client2);

        List<Client> result = clientRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getUsername());
        assertEquals("Bob", result.get(1).getUsername());
    }

    @Test
    public void testUpdateClient() {
        ClientType type1 = new DiamondClientType();
        Client client = new Client(4, "Alice", type1);
        clientRepository.add(client);

        client.setUsername("AliceUpdated");
        clientRepository.update(client);

        Client updatedClient = clientRepository.findById(client.getId());

        assertEquals("AliceUpdated", updatedClient.getUsername());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client(5, "Charlie", new BronzeClientType());
        clientRepository.add(client);

        clientRepository.delete(client.getId());

        Client deletedClient = clientRepository.findById(client.getId());

        assertNull(deletedClient);
    }
}
