package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Column(nullable = false, unique = true, length = 20)
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false, length = 50)
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false, length = 50)
    private String modele;

    @NotNull(message = "La catégorie est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategorieVehicule categorie;

    @NotNull(message = "Le tarif journalier est obligatoire")
    @Positive(message = "Le tarif journalier doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifJournalier;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVehicule statut;

    /** * Vehicule -> 1 Agence */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_agence", nullable = false)
    private Agence agence;

    /** * Vehicule <-> * Equipement (côté propriétaire : table de jointure vehicule_equipement) */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "id_vehicule"),
            inverseJoinColumns = @JoinColumn(name = "id_equipement"))
    private Set<Equipement> equipements = new HashSet<>();

    /** 1 Vehicule -> * Maintenance */
    @JsonIgnore
    @OneToMany(mappedBy = "vehicule")
    private List<Maintenance> maintenances = new ArrayList<>();

    /** 1 Vehicule -> * Reservation */
    @JsonIgnore
    @OneToMany(mappedBy = "vehicule")
    private List<Reservation> reservations = new ArrayList<>();
}
