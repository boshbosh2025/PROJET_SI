package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.EquipementRepository;
import tn.esprit.autoloc.service.EquipementService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements EquipementService {

    private final EquipementRepository equipementRepository;

    @Override
    public Equipement create(Equipement equipement) {
        equipement.setIdEquipement(null);
        return equipementRepository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement", id));
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        Equipement existant = findById(id);
        existant.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        equipementRepository.delete(findById(id));
    }
}
