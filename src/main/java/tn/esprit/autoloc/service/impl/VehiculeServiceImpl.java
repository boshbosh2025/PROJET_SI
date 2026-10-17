package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.DuplicateResourceException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.AgenceRepository;
import tn.esprit.autoloc.repository.EquipementRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;
import tn.esprit.autoloc.service.VehiculeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final AgenceRepository agenceRepository;
    private final EquipementRepository equipementRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        if (vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new DuplicateResourceException(
                    "Un véhicule existe déjà avec l'immatriculation " + vehicule.getImmatriculation());
        }
        vehicule.setIdVehicule(null);
        vehicule.setAgence(resolveAgence(vehicule.getAgence()));
        vehicule.setEquipements(resolveEquipements(vehicule.getEquipements()));
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", id));
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        if (!existant.getImmatriculation().equals(vehicule.getImmatriculation())
                && vehiculeRepository.existsByImmatriculation(vehicule.getImmatriculation())) {
            throw new DuplicateResourceException(
                    "Un véhicule existe déjà avec l'immatriculation " + vehicule.getImmatriculation());
        }
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(resolveAgence(vehicule.getAgence()));
        existant.setEquipements(resolveEquipements(vehicule.getEquipements()));
        return vehiculeRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        vehiculeRepository.delete(findById(id));
    }

    private Agence resolveAgence(Agence agence) {
        if (agence == null || agence.getIdAgence() == null) {
            throw new IllegalArgumentException("L'agence est obligatoire (agence.idAgence)");
        }
        return agenceRepository.findById(agence.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agence.getIdAgence()));
    }

    /** Charge les équipements depuis la base à partir de leurs ids (ex: [{"idEquipement": 1}]). */
    private Set<Equipement> resolveEquipements(Set<Equipement> equipements) {
        Set<Equipement> resolus = new HashSet<>();
        if (equipements == null) {
            return resolus;
        }
        for (Equipement e : equipements) {
            if (e.getIdEquipement() == null) {
                throw new IllegalArgumentException("Chaque équipement doit avoir un idEquipement");
            }
            resolus.add(equipementRepository.findById(e.getIdEquipement())
                    .orElseThrow(() -> new ResourceNotFoundException("Équipement", e.getIdEquipement())));
        }
        return resolus;
    }
}
