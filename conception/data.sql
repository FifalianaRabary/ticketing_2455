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


-- vue
CREATE OR REPLACE VIEW Promotions_Disponibles AS
SELECT
    p.id AS id_promotion,
    p.id_vol,
    p.id_type_siege,
    p.nb_siege - COALESCE(
        (SELECT COUNT(*) 
         FROM Detail_reservation dr 
         JOIN Reservation r ON dr.id_reservation = r.id
         WHERE r.id_vol = p.id_vol 
         AND dr.id_type_siege = p.id_type_siege),
    0) AS nb_promotions_dispo
FROM Promotion_siege p;



CREATE OR REPLACE VIEW montant_total_reservation AS
SELECT 
    r.id AS id_reservation,
    COALESCE(SUM(dr.montant), 0) AS montant_total
FROM 
    Reservation r
LEFT JOIN 
    Detail_reservation dr ON r.id = dr.id_reservation
GROUP BY 
    r.id;


-- CREATE OR REPLACE VIEW places_disponibles_vol AS
-- SELECT 
--     v.id AS id_vol,
--     v.designation AS vol,
--     a.designation AS avion,
--     COUNT(dr.id) AS places_occupees,
--     (SELECT capacite FROM Modele WHERE id = a.id_modele) AS capacite_totale,
--     (SELECT capacite FROM Modele WHERE id = a.id_modele) - COUNT(dr.id) AS places_restantes
-- FROM 
--     Vol v
-- JOIN 
--     Avion a ON v.id_avion = a.id
-- LEFT JOIN 
--     Reservation r ON v.id = r.id_vol
-- LEFT JOIN 
--     Detail_reservation dr ON r.id = dr.id_reservation
-- GROUP BY 
--     v.id, v.designation, a.designation, a.id_modele;


-- CREATE OR REPLACE VIEW places_disponibles_par_type AS
-- SELECT 
--     v.id AS id_vol,
--     v.designation AS vol,
--     ts.id AS id_type_siege,
--     ts.designation AS type_siege,
--     COUNT(dr.id) FILTER (WHERE dr.id_type_siege = ts.id) AS places_occupees,
--     ts.nombre_sieges AS capacite_type,
--     ts.nombre_sieges - COUNT(dr.id) FILTER (WHERE dr.id_type_siege = ts.id) AS places_restantes
-- FROM 
--     Vol v
-- CROSS JOIN 
--     Type_siege ts
-- LEFT JOIN 
--     Reservation r ON v.id = r.id_vol
-- LEFT JOIN 
--     Detail_reservation dr ON r.id = dr.id_reservation AND dr.id_type_siege = ts.id
-- GROUP BY 
--     v.id, v.designation, ts.id, ts.designation, ts.nombre_sieges
-- HAVING 
--     ts.nombre_sieges > 0;

-- 2nd

-- CREATE OR REPLACE VIEW places_disponibles_vol AS
-- SELECT 
--     v.id AS id_vol,
--     v.designation AS vol,
--     a.designation AS avion,
--     COUNT(dr.id) AS places_occupees,
--     (SELECT capacite FROM Modele WHERE id = a.id_modele) AS capacite_totale,
--     (SELECT capacite FROM Modele WHERE id = a.id_modele) - COUNT(dr.id) AS places_restantes
-- FROM 
--     Vol v
-- JOIN 
--     Avion a ON v.id_avion = a.id
-- LEFT JOIN 
--     Reservation r ON v.id = r.id_vol AND r.complete = true
-- LEFT JOIN 
--     Detail_reservation dr ON r.id = dr.id_reservation
-- GROUP BY 
--     v.id, v.designation, a.designation, a.id_modele;


-- CREATE OR REPLACE VIEW places_disponibles_par_type AS
-- SELECT 
--     v.id AS id_vol,
--     v.designation AS vol,
--     ts.id AS id_type_siege,
--     ts.designation AS type_siege,
--     COUNT(dr.id) FILTER (WHERE dr.id_type_siege = ts.id) AS places_occupees,
--     ts.nombre_sieges AS capacite_type,
--     ts.nombre_sieges - COUNT(dr.id) FILTER (WHERE dr.id_type_siege = ts.id) AS places_restantes
-- FROM 
--     Vol v
-- CROSS JOIN 
--     Type_siege ts
-- LEFT JOIN 
--     Reservation r ON v.id = r.id_vol AND r.complete = true
-- LEFT JOIN 
--     Detail_reservation dr ON r.id = dr.id_reservation AND dr.id_type_siege = ts.id
-- GROUP BY 
--     v.id, v.designation, ts.id, ts.designation, ts.nombre_sieges
-- HAVING 
--     ts.nombre_sieges > 0;
