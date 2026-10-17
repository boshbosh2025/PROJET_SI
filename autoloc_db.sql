-- ============================================================
-- AutoLoc - Base MySQL conforme au diagramme de classes
-- 9 entités -> 10 tables (dont la table de jointure vehicule_equipement)
-- Import : mysql -u root -p < autoloc_db.sql
--      ou MySQL Workbench / phpMyAdmin > Import
-- ============================================================

DROP DATABASE IF EXISTS autoloc_db;
CREATE DATABASE autoloc_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE autoloc_db;

-- ---------- Tables ----------

CREATE TABLE agence (
    id_agence  BIGINT       NOT NULL AUTO_INCREMENT,
    nom        VARCHAR(100) NOT NULL,
    ville      VARCHAR(80)  NOT NULL,
    adresse    VARCHAR(150) NOT NULL,
    telephone  VARCHAR(20),
    PRIMARY KEY (id_agence)
) ENGINE=InnoDB;

CREATE TABLE employe (
    id_employe BIGINT      NOT NULL AUTO_INCREMENT,
    nom        VARCHAR(50) NOT NULL,
    prenom     VARCHAR(50) NOT NULL,
    role       VARCHAR(20) NOT NULL,               -- AGENT | MANAGER
    id_agence  BIGINT      NOT NULL,
    PRIMARY KEY (id_employe),
    CONSTRAINT fk_employe_agence FOREIGN KEY (id_agence) REFERENCES agence (id_agence)
) ENGINE=InnoDB;

CREATE TABLE vehicule (
    id_vehicule      BIGINT        NOT NULL AUTO_INCREMENT,
    immatriculation  VARCHAR(20)   NOT NULL,
    marque           VARCHAR(50)   NOT NULL,
    modele           VARCHAR(50)   NOT NULL,
    categorie        VARCHAR(20)   NOT NULL,       -- CITADINE | BERLINE | SUV | UTILITAIRE
    tarif_journalier DECIMAL(10,2) NOT NULL,
    statut           VARCHAR(20)   NOT NULL,       -- DISPONIBLE | LOUE | MAINTENANCE
    id_agence        BIGINT        NOT NULL,
    PRIMARY KEY (id_vehicule),
    UNIQUE KEY uk_vehicule_immat (immatriculation),
    CONSTRAINT fk_vehicule_agence FOREIGN KEY (id_agence) REFERENCES agence (id_agence)
) ENGINE=InnoDB;

CREATE TABLE equipement (
    id_equipement BIGINT       NOT NULL AUTO_INCREMENT,
    libelle       VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_equipement)
) ENGINE=InnoDB;

-- Association * Vehicule <-> * Equipement
CREATE TABLE vehicule_equipement (
    id_vehicule   BIGINT NOT NULL,
    id_equipement BIGINT NOT NULL,
    PRIMARY KEY (id_vehicule, id_equipement),
    CONSTRAINT fk_ve_vehicule   FOREIGN KEY (id_vehicule)   REFERENCES vehicule (id_vehicule),
    CONSTRAINT fk_ve_equipement FOREIGN KEY (id_equipement) REFERENCES equipement (id_equipement)
) ENGINE=InnoDB;

CREATE TABLE maintenance (
    id_maintenance BIGINT       NOT NULL AUTO_INCREMENT,
    date_debut     DATE         NOT NULL,
    date_fin       DATE,
    description    VARCHAR(255),
    id_vehicule    BIGINT       NOT NULL,
    PRIMARY KEY (id_maintenance),
    CONSTRAINT fk_maintenance_vehicule FOREIGN KEY (id_vehicule) REFERENCES vehicule (id_vehicule)
) ENGINE=InnoDB;

CREATE TABLE client (
    id_client        BIGINT       NOT NULL AUTO_INCREMENT,
    nom              VARCHAR(50)  NOT NULL,
    prenom           VARCHAR(50)  NOT NULL,
    email            VARCHAR(100) NOT NULL,
    telephone        VARCHAR(20),
    num_permis       VARCHAR(30),
    date_inscription DATE,
    PRIMARY KEY (id_client),
    UNIQUE KEY uk_client_email (email),
    UNIQUE KEY uk_client_permis (num_permis)
) ENGINE=InnoDB;

CREATE TABLE reservation (
    id_reservation BIGINT      NOT NULL AUTO_INCREMENT,
    date_debut     DATE        NOT NULL,
    date_fin       DATE        NOT NULL,
    statut         VARCHAR(20) NOT NULL,           -- EN_ATTENTE | CONFIRMEE | ANNULEE | TERMINEE
    id_client      BIGINT      NOT NULL,
    id_vehicule    BIGINT      NOT NULL,
    PRIMARY KEY (id_reservation),
    CONSTRAINT fk_resa_client   FOREIGN KEY (id_client)   REFERENCES client (id_client),
    CONSTRAINT fk_resa_vehicule FOREIGN KEY (id_vehicule) REFERENCES vehicule (id_vehicule)
) ENGINE=InnoDB;

-- Association 1 Reservation <-> 1 Contrat
CREATE TABLE contrat (
    id_contrat      BIGINT        NOT NULL AUTO_INCREMENT,
    date_signature  DATE,
    montant_total   DECIMAL(10,2) NOT NULL,
    valide          BIT           NOT NULL,
    id_reservation  BIGINT        NOT NULL,
    PRIMARY KEY (id_contrat),
    UNIQUE KEY uk_contrat_reservation (id_reservation),
    CONSTRAINT fk_contrat_resa FOREIGN KEY (id_reservation) REFERENCES reservation (id_reservation)
) ENGINE=InnoDB;

-- Composition 1 Contrat -> * Paiement
CREATE TABLE paiement (
    id_paiement    BIGINT        NOT NULL AUTO_INCREMENT,
    montant        DECIMAL(10,2) NOT NULL,
    date_paiement  DATE,
    mode_paiement  VARCHAR(20)   NOT NULL,         -- CARTE | ESPECES | VIREMENT
    id_contrat     BIGINT        NOT NULL,
    PRIMARY KEY (id_paiement),
    CONSTRAINT fk_paiement_contrat FOREIGN KEY (id_contrat) REFERENCES contrat (id_contrat) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------- Données de test ----------

INSERT INTO agence (nom, ville, adresse, telephone) VALUES
('AutoLoc Tunis Centre', 'Tunis',  '12 Avenue Habib Bourguiba',  '71123456'),
('AutoLoc Sousse',       'Sousse', '5 Boulevard du 14 Janvier',  '73456789'),
('AutoLoc Sfax',         'Sfax',   '20 Route de Gabès',          '74987654');

INSERT INTO employe (nom, prenom, role, id_agence) VALUES
('Mansouri', 'Hatem',  'MANAGER', 1),
('Jlassi',   'Sonia',  'AGENT',   1),
('Chaabane', 'Walid',  'MANAGER', 2),
('Dridi',    'Ines',   'AGENT',   3);

INSERT INTO vehicule (immatriculation, marque, modele, categorie, tarif_journalier, statut, id_agence) VALUES
('200 TUN 1234', 'Peugeot',    '208',    'CITADINE',   85.00, 'DISPONIBLE', 1),
('195 TUN 5678', 'Renault',    'Megane', 'BERLINE',    95.00, 'LOUE',       1),
('210 TUN 4321', 'Kia',        'Picanto','CITADINE',   65.00, 'DISPONIBLE', 2),
('188 TUN 8765', 'Toyota',     'RAV4',   'SUV',       140.00, 'MAINTENANCE',2),
('205 TUN 1111', 'Renault',    'Kangoo', 'UTILITAIRE',110.00, 'DISPONIBLE', 3);

INSERT INTO equipement (libelle) VALUES
('GPS'), ('Siège bébé'), ('Climatisation'), ('Bluetooth');

INSERT INTO vehicule_equipement (id_vehicule, id_equipement) VALUES
(1, 1), (1, 3), (2, 3), (2, 4), (4, 1), (4, 2), (5, 3);

INSERT INTO maintenance (date_debut, date_fin, description, id_vehicule) VALUES
('2026-10-12', NULL,         'Révision complète et vidange', 4),
('2026-08-01', '2026-08-02', 'Changement de pneus',          1);

INSERT INTO client (nom, prenom, email, telephone, num_permis, date_inscription) VALUES
('Ben Ali',  'Sami',  'sami.benali@mail.tn',    '98123456', 'P-0012345', '2025-03-10'),
('Trabelsi', 'Leila', 'leila.trabelsi@mail.tn', '97234567', 'P-0023456', '2025-11-02'),
('Gharbi',   'Karim', 'karim.gharbi@mail.tn',   '96345678', 'P-0034567', '2026-01-15');

INSERT INTO reservation (date_debut, date_fin, statut, id_client, id_vehicule) VALUES
('2026-10-10', '2026-10-15', 'CONFIRMEE',  1, 2),
('2026-10-20', '2026-10-23', 'EN_ATTENTE', 2, 3),
('2026-09-01', '2026-09-04', 'TERMINEE',   3, 5);

INSERT INTO contrat (date_signature, montant_total, valide, id_reservation) VALUES
('2026-10-09', 475.00, b'1', 1),
('2026-09-01', 330.00, b'1', 3);

INSERT INTO paiement (montant, date_paiement, mode_paiement, id_contrat) VALUES
(200.00, '2026-10-09', 'CARTE',    1),
(275.00, '2026-10-15', 'ESPECES',  1),
(330.00, '2026-09-04', 'VIREMENT', 2);

-- Vérification : doit afficher 10 tables
-- SHOW TABLES;
