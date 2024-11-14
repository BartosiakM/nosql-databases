package com.rental.repository;

import com.rental.model.BronzeClientType;
import com.rental.model.Client;
import com.rental.model.ClientType;
import com.rental.model.DefaultClientType;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        clientRepository.getDatabase().getCollection("clients").deleteMany(new org.bson.Document());
    }

    @Test
    public void testAddClient() {
        ClientType type = new BronzeClientType();
        Client client = new Client(UUID.randomUUID(), "JohnDoe", type);

        Client result = clientRepository.add(client);

        assertNotNull(result);
        Optional<Client> foundClient = clientRepository.findById(client.getClientId());
        assertTrue(foundClient.isPresent());
        assertEquals(client.getClientId(), foundClient.get().getClientId());
    }

    @Test
    public void testFindById() {
        ClientType type = new BronzeClientType();
        Client client = new Client(UUID.randomUUID(), "JaneDoe", type);
        clientRepository.add(client);

        Optional<Client> result = clientRepository.findById(client.getClientId());

        assertTrue(result.isPresent());
        assertEquals(client.getUsername(), result.get().getUsername());
    }

    @Test
    public void testFindByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        Optional<Client> result = clientRepository.findById(randomId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAll() {
        Client client1 = new Client(UUID.randomUUID(), "Alice", new BronzeClientType());
        Client client2 = new Client(UUID.randomUUID(), "Bob", new DefaultClientType());

        clientRepository.add(client1);
        clientRepository.add(client2);

        List<Client> result = clientRepository.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getUsername());
        assertEquals("Bob", result.get(1).getUsername());
    }

    @Test
    public void testUpdateClient() {
        ClientType type1 = new BronzeClientType();
        Client client = new Client(UUID.randomUUID(), "Alice", type1);
        clientRepository.add(client);

        client.setUsername("AliceUpdated");

        clientRepository.update(client);

        Optional<Client> updatedClient = clientRepository.findById(client.getClientId());

        assertTrue(updatedClient.isPresent());
        assertEquals("AliceUpdated", updatedClient.get().getUsername());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client(UUID.randomUUID(), "Charlie", new BronzeClientType());
        clientRepository.add(client);

        clientRepository.delete(client.getClientId());

        Optional<Client> deletedClient = clientRepository.findById(client.getClientId());

        assertFalse(deletedClient.isPresent());
    }
}
