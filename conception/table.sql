-- Création de la table Modèle
CREATE TABLE Modele (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Avion
CREATE TABLE Avion (
    id SERIAL PRIMARY KEY,
    id_modele INT REFERENCES Modele(id) ON DELETE CASCADE,
    nb_siege_business INT CHECK (nb_siege_business >= 0),
    nb_siege_eco INT CHECK (nb_siege_eco >= 0),
    date_fabrication DATE NOT NULL
);

-- Création de la table Type_siege
CREATE TABLE Type_siege (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table Ville
CREATE TABLE Ville (
    id SERIAL PRIMARY KEY,
    designation VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Vol
CREATE TABLE Vol (
    id SERIAL PRIMARY KEY,
    date_heure_depart TIMESTAMP NOT NULL,
    date_heure_arrivee TIMESTAMP NOT NULL CHECK (date_heure_arrivee > date_heure_depart),
    id_avion INT REFERENCES Avion(id) ON DELETE SET NULL,
    id_ville INT REFERENCES Ville(id) ON DELETE CASCADE,
    prix_business DECIMAL(10,2) CHECK (prix_business >= 0),
    prix_eco DECIMAL(10,2) CHECK (prix_eco >= 0)
);

-- Création de la table Promotion_siege
CREATE TABLE Promotion_siege (
    id SERIAL PRIMARY KEY,
    pourcent DECIMAL(5,2) CHECK (pourcent >= 0 AND pourcent <= 100),
    id_vol INT REFERENCES Vol(id) ON DELETE CASCADE,
    id_type_siege INT REFERENCES Type_siege(id) ON DELETE CASCADE,
    nb_siege INT CHECK (nb_siege >= 0)
);

-- Création de la table Regle_reservation
CREATE TABLE Regle_reservation (
    id SERIAL PRIMARY KEY,
    id_vol INT REFERENCES Vol(id) ON DELETE CASCADE,
    nb_heure_limite_avant_vol INT CHECK (nb_heure_limite_avant_vol >= 0)
);

-- Création de la table Regle_annulation_reservation
CREATE TABLE Regle_annulation_reservation (
    id SERIAL PRIMARY KEY,
    id_vol INT REFERENCES Vol(id) ON DELETE CASCADE,
    nb_heure_limite_avant_vol INT CHECK (nb_heure_limite_avant_vol >= 0)
);

-- Création de la table Utilisateur
CREATE TABLE Utilisateur (
    id SERIAL PRIMARY KEY,
    mail VARCHAR(255) NOT NULL UNIQUE,
    mdp TEXT NOT NULL
);

-- Création de la table Reservation
CREATE TABLE Reservation (
    id SERIAL PRIMARY KEY,
    id_user INT REFERENCES Utilisateur(id) ON DELETE CASCADE,
    date_heure_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_vol INT REFERENCES Vol(id) ON DELETE CASCADE,
    id_type_siege INT REFERENCES Type_siege(id) ON DELETE CASCADE,
    montant DECIMAL(10,2) CHECK (montant >= 0)
);
