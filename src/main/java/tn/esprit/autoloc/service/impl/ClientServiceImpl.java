package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.DuplicateResourceException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ClientRepository;
import tn.esprit.autoloc.service.ClientService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new DuplicateResourceException("Un client existe déjà avec l'email " + client.getEmail());
        }
        if (client.getNumPermis() != null && clientRepository.existsByNumPermis(client.getNumPermis())) {
            throw new DuplicateResourceException(
                    "Un client existe déjà avec le numéro de permis " + client.getNumPermis());
        }
        client.setIdClient(null);
        if (client.getDateInscription() == null) {
            client.setDateInscription(LocalDate.now());
        }
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public Client update(Long id, Client client) {
        Client existant = findById(id);
        if (!existant.getEmail().equals(client.getEmail())
                && clientRepository.existsByEmail(client.getEmail())) {
            throw new DuplicateResourceException("Un client existe déjà avec l'email " + client.getEmail());
        }
        if (client.getNumPermis() != null
                && !client.getNumPermis().equals(existant.getNumPermis())
                && clientRepository.existsByNumPermis(client.getNumPermis())) {
            throw new DuplicateResourceException(
                    "Un client existe déjà avec le numéro de permis " + client.getNumPermis());
        }
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        if (client.getDateInscription() != null) {
            existant.setDateInscription(client.getDateInscription());
        }
        return clientRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        clientRepository.delete(findById(id));
    }
}
