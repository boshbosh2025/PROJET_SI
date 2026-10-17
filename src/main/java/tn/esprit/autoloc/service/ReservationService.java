package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation create(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation update(Long id, Reservation reservation);

    void delete(Long id);
}
