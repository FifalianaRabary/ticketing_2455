insert into Utilisateur(username,mdp,level) values ('fifaliana','admin','admin');
insert into Utilisateur(username,mdp,level) values ('fifa','fifa','user');


-- Insertion des modèles d'avions
INSERT INTO Modele (designation) VALUES 
('Boeing 737'),
('Airbus A320'),
('Boeing 777');

-- Insertion des types de sièges (Économie et Business uniquement)
INSERT INTO Type_siege (designation) VALUES 
('Economie'),
('Business');

-- Insertion des avions avec des modèles associés
INSERT INTO Avion (designation,id_modele, date_fabrication) VALUES 
('avion1',1, '2015-06-10'),
('avion2',2, '2018-03-15'),
('avion3',3, '2020-07-22');

-- Insertion des sièges pour chaque avion (nombre aléatoire)
-- Boeing 737
INSERT INTO Siege_avion (id_avion, id_type_siege, nb) VALUES 
(1, 1, 120), -- 120 sièges économie
(1, 2, 20);  -- 20 sièges business

-- Airbus A320
INSERT INTO Siege_avion (id_avion, id_type_siege, nb) VALUES 
(2, 1, 150), -- 150 sièges économie
(2, 2, 24);  -- 24 sièges business

-- Boeing 777
INSERT INTO Siege_avion (id_avion, id_type_siege, nb) VALUES 
(3, 1, 250), -- 250 sièges économie
(3, 2, 50);  -- 50 sièges business

-- Insertion des villes
INSERT INTO Ville (designation) VALUES 
('Paris'),
('New York'),
('Tokyo'),
('Londres'),
('Dubai'),
('Los Angeles');

