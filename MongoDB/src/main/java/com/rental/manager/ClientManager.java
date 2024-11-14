package com.rental.manager;

import java.io.Serializable;
import java.util.UUID;

import com.rental.model.Client;
import com.rental.repository.ClientRepository;

public class ClientManager implements Serializable {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new NullPointerException("clientRepository is null");
        }
        this.clientRepository = clientRepository;
    }

    public void removeClient(UUID ID) {
        clientRepository.delete(ID);
    }

    public Client addClient(Client client) {
        if (clientRepository.findById(client.getClientId()).isPresent()) {
            throw new IllegalArgumentException("Client already exists");
        }
        return clientRepository.add(client);
    }
}
