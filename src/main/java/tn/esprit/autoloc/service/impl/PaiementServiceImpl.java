package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ContratRepository;
import tn.esprit.autoloc.repository.PaiementRepository;
import tn.esprit.autoloc.service.PaiementService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    @Override
    public Paiement create(Paiement paiement) {
        paiement.setIdPaiement(null);
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }
        paiement.setContrat(resolveContrat(paiement.getContrat()));
        return paiementRepository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    @Override
    public Paiement update(Long id, Paiement paiement) {
        Paiement existant = findById(id);
        existant.setMontant(paiement.getMontant());
        existant.setModePaiement(paiement.getModePaiement());
        if (paiement.getDatePaiement() != null) {
            existant.setDatePaiement(paiement.getDatePaiement());
        }
        existant.setContrat(resolveContrat(paiement.getContrat()));
        return paiementRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        paiementRepository.delete(findById(id));
    }

    private Contrat resolveContrat(Contrat contrat) {
        if (contrat == null || contrat.getIdContrat() == null) {
            throw new IllegalArgumentException("Le contrat est obligatoire (contrat.idContrat)");
        }
        return contratRepository.findById(contrat.getIdContrat())
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", contrat.getIdContrat()));
    }
}
