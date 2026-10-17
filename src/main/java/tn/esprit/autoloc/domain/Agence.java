package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    @NotBlank(message = "Le nom de l'agence est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    @Column(nullable = false, length = 80)
    private String ville;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(nullable = false, length = 150)
    private String adresse;

    @Column(length = 20)
    private String telephone;

    /** 1 Agence -> * Employe */
    @JsonIgnore
    @OneToMany(mappedBy = "agence")
    private List<Employe> employes = new ArrayList<>();

    /** 1 Agence -> * Vehicule */
    @JsonIgnore
    @OneToMany(mappedBy = "agence")
    private List<Vehicule> vehicules = new ArrayList<>();
}
