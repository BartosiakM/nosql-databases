package com.rental.repository;

import jakarta.persistence.*;
import java.util.List;
import com.rental.model.Client;

public class ClientRepository implements Repository<Client> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("NBDUnit");

    @Override
    public Client getByID(Long ID) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Client.class, ID);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Client> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Client add(Client client) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(client);
            transaction.commit();
            return client;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Client client) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Client attachedClient = em.find(Client.class, client.getClientId());
            if (attachedClient != null) {
                em.remove(attachedClient);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Client client) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(client);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
