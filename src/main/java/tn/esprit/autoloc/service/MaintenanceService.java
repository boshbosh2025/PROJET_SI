package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface MaintenanceService {

    Maintenance create(Maintenance maintenance);

    List<Maintenance> findAll();

    Maintenance findById(Long id);

    Maintenance update(Long id, Maintenance maintenance);

    void delete(Long id);
}
