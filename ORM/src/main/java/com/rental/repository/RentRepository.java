package com.rental.repository;

import com.rental.model.Client;
import com.rental.model.Rent;
import com.rental.model.Vehicle;
import jakarta.persistence.*;

import java.util.List;

public class RentRepository implements Repository<Rent> {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("NBDUnit");

    @Override
    public Rent getByID(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Rent.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rent> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Rent> query = em.createQuery("SELECT r FROM Rent r", Rent.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Rent add(Rent rent) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Vehicle rentedVehicle = em.find(Vehicle.class, rent.getVehicle().getVehicleId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Client rentingClient = em.find(Client.class, rent.getClient().getClientId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            if (rentingClient.getMaxVehicles() <= rentingClient.getActiveRents()) {
                throw new IllegalStateException("This client has already reached the maximum number of active rentals.");
            }
            if (!rentedVehicle.isAvailable()) {
                throw new IllegalStateException("This vehicle is not available");
            }
            rentedVehicle.setAvailable(false);
            rentingClient.setActiveRents(rentingClient.getActiveRents() + 1);

            em.merge(rentingClient);
            em.merge(rentedVehicle);
            em.persist(rent);

            transaction.commit();
            em.refresh(rent);
            return rent;
        } catch (OptimisticLockException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Vehicle has just been rented");
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
    public void remove(Rent rent) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Rent attachedRent = em.find(Rent.class, rent.getRentId());
            if (attachedRent != null) {
                em.remove(attachedRent);
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
    public void update(Rent rent) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(rent);
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
