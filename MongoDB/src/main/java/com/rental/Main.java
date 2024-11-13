package com.rental;

import com.rental.manager.ClientManager;
import com.rental.model.Address;
import com.rental.model.Client;
import com.rental.model.BronzeClientType;
import com.rental.model.ClientType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NBDUnit");

        EntityManager em = emf.createEntityManager(); ;
        ClientManager manager = new ClientManager((EntityManager) em);
        Address addres  =  new Address("lodz", "sdasdf", "asdsa");
        ClientType clientType = new BronzeClientType();
        Client client = manager.addClient("lala Jan Kowalski", clientType, addres);
    }

}
