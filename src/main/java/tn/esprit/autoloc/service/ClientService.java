package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Client;

import java.util.List;

public interface ClientService {

    Client create(Client client);

    List<Client> findAll();

    Client findById(Long id);

    Client update(Long id, Client client);

    void delete(Long id);
}
