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
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.AgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences de location")
public class AgenceController {

    private final AgenceService agenceService;

    @Operation(summary = "Lister toutes les agences")
    @ApiResponse(responseCode = "200", description = "Liste des agences")
    @GetMapping
    public List<Agence> getAll() {
        return agenceService.findAll();
    }

    @Operation(summary = "Récupérer une agence par son id")
    @ApiResponse(responseCode = "200", description = "Agence trouvée")
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content)
    @GetMapping("/{id}")
    public Agence getById(@PathVariable Long id) {
        return agenceService.findById(id);
    }

    @Operation(summary = "Créer une agence")
    @ApiResponse(responseCode = "201", description = "Agence créée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @PostMapping
    public ResponseEntity<Agence> create(@Valid @RequestBody Agence agence) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenceService.create(agence));
    }

    @Operation(summary = "Modifier une agence")
    @ApiResponse(responseCode = "200", description = "Agence modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content)
    @PutMapping("/{id}")
    public Agence update(@PathVariable Long id, @Valid @RequestBody Agence agence) {
        return agenceService.update(id, agence);
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponse(responseCode = "204", description = "Agence supprimée")
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Agence encore référencée (véhicules, employés)", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        agenceService.delete(id);
    }
}
