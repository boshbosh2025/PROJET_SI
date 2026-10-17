package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.DuplicateResourceException;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ContratRepository;
import tn.esprit.autoloc.repository.ReservationRepository;
import tn.esprit.autoloc.service.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements ContratService {

    private final ContratRepository contratRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Contrat create(Contrat contrat) {
        contrat.setIdContrat(null);
        Reservation reservation = resolveReservation(contrat.getReservation());
        if (contratRepository.existsByReservationIdReservation(reservation.getIdReservation())) {
            throw new DuplicateResourceException(
                    "La réservation " + reservation.getIdReservation() + " possède déjà un contrat");
        }
        contrat.setReservation(reservation);
        if (contrat.getDateSignature() == null) {
            contrat.setDateSignature(LocalDate.now());
        }
        if (contrat.getMontantTotal() == null) {
            contrat.setMontantTotal(calculerMontant(reservation));
        }
        return contratRepository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        Contrat existant = findById(id);
        existant.setDateSignature(contrat.getDateSignature());
        if (contrat.getMontantTotal() != null) {
            existant.setMontantTotal(contrat.getMontantTotal());
        }
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        contratRepository.delete(findById(id));
    }

    private Reservation resolveReservation(Reservation reservation) {
        if (reservation == null || reservation.getIdReservation() == null) {
            throw new IllegalArgumentException("La réservation est obligatoire (reservation.idReservation)");
        }
        return reservationRepository.findById(reservation.getIdReservation())
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", reservation.getIdReservation()));
    }

    /** Montant = nombre de jours (minimum 1) x tarif journalier du véhicule. */
    private BigDecimal calculerMontant(Reservation r) {
        long jours = Math.max(1, ChronoUnit.DAYS.between(r.getDateDebut(), r.getDateFin()));
        return r.getVehicule().getTarifJournalier().multiply(BigDecimal.valueOf(jours));
    }
}
