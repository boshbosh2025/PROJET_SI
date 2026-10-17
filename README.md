# AutoLoc API

Plateforme de gestion de location de véhicules multi-agences (Spring Boot 3.3.5, Java 17, MySQL).

## Modèle (diagramme de classes)

| Association | Implémentation JPA |
|---|---|
| Agence 1 — * Employe | `@ManyToOne` dans `Employe` / `@OneToMany(mappedBy)` dans `Agence` |
| Agence 1 — * Vehicule | `@ManyToOne` dans `Vehicule` / `@OneToMany(mappedBy)` dans `Agence` |
| Vehicule 1 — * Maintenance | `@ManyToOne` dans `Maintenance` |
| Vehicule * — * Equipement | `@ManyToMany` + `@JoinTable(vehicule_equipement)` côté `Vehicule` |
| Client 1 — * Reservation | `@ManyToOne` dans `Reservation` |
| Vehicule 1 — * Reservation | `@ManyToOne` dans `Reservation` |
| Reservation 1 — 1 Contrat | `@OneToOne` (clé étrangère `id_reservation`, unique) dans `Contrat` |
| Contrat 1 ◆— * Paiement (composition) | `@OneToMany(cascade = ALL, orphanRemoval = true)` dans `Contrat` |

**9 entités → 10 tables** : agence, employe, vehicule, equipement, vehicule_equipement, maintenance, client, reservation, contrat, paiement.

## Procédure Atelier 2 (régénérer la base)

1. Supprimer les tables existantes : dans `application.properties`, mettre `spring.jpa.hibernate.ddl-auto=create`.
2. Lancer l'application une fois (`mvn spring-boot:run`) : Hibernate supprime puis recrée toutes les tables.
3. Vérifier dans MySQL : `USE autoloc_db; SHOW TABLES;` → **10 tables**.
4. Arrêter l'application et remettre `spring.jpa.hibernate.ddl-auto=update`.

(Alternative sans Hibernate : importer `autoloc_db.sql`, qui crée les mêmes 10 tables avec des données de test.)

## Lancer le projet

1. Renseigner `spring.datasource.password` dans `src/main/resources/application.properties`.
2. `mvn spring-boot:run`
3. Swagger UI : http://localhost:8080/swagger-ui.html

## Architecture

```
domain/          entités JPA + énumérations
repository/      9 JpaRepository
service/         9 interfaces        service/impl/  9 implémentations (CRUD complet)
web/controller/  AgenceController, VehiculeController, ClientController (@Operation / @ApiResponse)
exception/       404 / 409 / 400 gérés globalement
config/          OpenApiConfig
```

## Endpoints

`/api/agences`, `/api/vehicules`, `/api/clients` : GET, GET/{id}, POST, PUT/{id}, DELETE/{id}.

Rattachements dans le JSON :
- véhicule → agence : `"agence": { "idAgence": 1 }`
- véhicule → équipements : `"equipements": [ { "idEquipement": 1 } ]`

## Tests Postman

Importer `postman/AutoLoc.postman_collection.json` et lancer la collection dans l'ordre
(Agences → Vehicules → Clients → Nettoyage). Pour l'étape « équipement », importer d'abord `autoloc_db.sql`.

## Lancer sans installer Maven (Maven Wrapper)

Depuis le terminal d'IntelliJ (Windows PowerShell), à la racine du projet :

```
.\mvnw.cmd spring-boot:run
```

Le wrapper télécharge Maven automatiquement au premier lancement (connexion internet requise).
