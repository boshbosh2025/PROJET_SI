package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.AgenceRepository;
import tn.esprit.autoloc.repository.EmployeRepository;
import tn.esprit.autoloc.service.EmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepository employeRepository;
    private final AgenceRepository agenceRepository;

    @Override
    public Employe create(Employe employe) {
        employe.setIdEmploye(null);
        employe.setAgence(resolveAgence(employe.getAgence()));
        return employeRepository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", id));
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(resolveAgence(employe.getAgence()));
        return employeRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        employeRepository.delete(findById(id));
    }

    private Agence resolveAgence(Agence agence) {
        if (agence == null || agence.getIdAgence() == null) {
            throw new IllegalArgumentException("L'agence est obligatoire (agence.idAgence)");
        }
        return agenceRepository.findById(agence.getIdAgence())
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agence.getIdAgence()));
    }
}
