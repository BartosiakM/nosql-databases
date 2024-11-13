package com.rental.repository;

import com.rental.model.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VehicleRepository implements Repository<Vehicle> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("NBDUnit");

    @Override
    public Vehicle getByID(Long ID) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Vehicle.class, ID);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
            return query.getResultList();
        }
    }

    @Override
    public Vehicle add(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(vehicle);
            transaction.commit();
            return vehicle;
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
    public void remove(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Vehicle attachedVehicle = em.find(Vehicle.class, vehicle.getVehicleId());
            if (attachedVehicle != null) {
                em.remove(attachedVehicle);
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
    public void update(Vehicle vehicle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(vehicle);
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
