package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @NotNull(message = "La date de début est obligatoire")
    @Column(nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @Column(nullable = false)
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    /** * Reservation -> 1 Client */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    /** * Reservation -> 1 Vehicule */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    /** 1 Reservation <-> 1 Contrat (côté inverse) */
    @JsonIgnore
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.REMOVE)
    private Contrat contrat;
}
