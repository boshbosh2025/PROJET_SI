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
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Lister tous les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients")
    @GetMapping
    public List<Client> getAll() {
        return clientService.findAll();
    }

    @Operation(summary = "Récupérer un client par son id")
    @ApiResponse(responseCode = "200", description = "Client trouvé")
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content)
    @GetMapping("/{id}")
    public Client getById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Créer un client", description = "dateInscription vaut la date du jour si elle est omise.")
    @ApiResponse(responseCode = "201", description = "Client créé")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email ou numéro de permis déjà utilisé", content = @Content)
    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(client));
    }

    @Operation(summary = "Modifier un client")
    @ApiResponse(responseCode = "200", description = "Client modifié")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email ou numéro de permis déjà utilisé", content = @Content)
    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @Valid @RequestBody Client client) {
        return clientService.update(id, client);
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponse(responseCode = "204", description = "Client supprimé")
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content)
    @ApiResponse(responseCode = "409", description = "Client encore référencé (réservations)", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}
