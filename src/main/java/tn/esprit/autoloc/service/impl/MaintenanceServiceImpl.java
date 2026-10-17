package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.MaintenanceRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;
import tn.esprit.autoloc.service.MaintenanceService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehiculeRepository vehiculeRepository;

    @Override
    public Maintenance create(Maintenance maintenance) {
        maintenance.setIdMaintenance(null);
        prepare(maintenance);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        Maintenance existante = findById(id);
        existante.setDateDebut(maintenance.getDateDebut());
        existante.setDateFin(maintenance.getDateFin());
        existante.setDescription(maintenance.getDescription());
        existante.setVehicule(maintenance.getVehicule());
        prepare(existante);
        return maintenanceRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        maintenanceRepository.delete(findById(id));
    }

    private void prepare(Maintenance m) {
        if (m.getDateFin() != null && m.getDateFin().isBefore(m.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
        if (m.getVehicule() == null || m.getVehicule().getIdVehicule() == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire (vehicule.idVehicule)");
        }
        Vehicule vehicule = vehiculeRepository.findById(m.getVehicule().getIdVehicule())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", m.getVehicule().getIdVehicule()));
        m.setVehicule(vehicule);
    }
}
