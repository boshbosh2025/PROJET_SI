package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ClientRepository;
import tn.esprit.autoloc.repository.ReservationRepository;
import tn.esprit.autoloc.repository.VehiculeRepository;
import tn.esprit.autoloc.service.ReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;

    @Override
    public Reservation create(Reservation reservation) {
        reservation.setIdReservation(null);
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        prepare(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existante = findById(id);
        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setClient(reservation.getClient());
        existante.setVehicule(reservation.getVehicule());
        if (reservation.getStatut() != null) {
            existante.setStatut(reservation.getStatut());
        }
        prepare(existante);
        return reservationRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.delete(findById(id));
    }

    private void prepare(Reservation r) {
        if (r.getDateFin().isBefore(r.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
        if (r.getClient() == null || r.getClient().getIdClient() == null) {
            throw new IllegalArgumentException("Le client est obligatoire (client.idClient)");
        }
        if (r.getVehicule() == null || r.getVehicule().getIdVehicule() == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire (vehicule.idVehicule)");
        }
        Client client = clientRepository.findById(r.getClient().getIdClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client", r.getClient().getIdClient()));
        Vehicule vehicule = vehiculeRepository.findById(r.getVehicule().getIdVehicule())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", r.getVehicule().getIdVehicule()));
        r.setClient(client);
        r.setVehicule(vehicule);
    }
}
