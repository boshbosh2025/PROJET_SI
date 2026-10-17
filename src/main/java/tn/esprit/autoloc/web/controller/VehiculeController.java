package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.VehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion du parc de véhicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @Operation(summary = "Lister tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules")
    @GetMapping
    public List<Vehicule> getAll() {
        return vehiculeService.findAll();
    }

    @Operation(summary = "Récupérer un véhicule par son id")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable", content = @Content)
    @GetMapping("/{id}")
    public Vehicule getById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Créer un véhicule",
            description = "L'agence est rattachée via son id ({\"agence\": {\"idAgence\": 1}}) "
                    + "et les équipements via leurs ids ({\"equipements\": [{\"idEquipement\": 1}]}).")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @ApiResponse(responseCode = "404", description = "Agence ou équipement introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Immatriculation déjà existante", content = @Content)
    @PostMapping
    public ResponseEntity<Vehicule> create(@Valid @RequestBody Vehicule vehicule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(vehicule));
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @ApiResponse(responseCode = "404", description = "Véhicule, agence ou équipement introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Immatriculation déjà existante", content = @Content)
    @PutMapping("/{id}")
    public Vehicule update(@PathVariable Long id, @Valid @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Véhicule encore référencé (réservations, maintenances)", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehiculeService.delete(id);
    }
}
