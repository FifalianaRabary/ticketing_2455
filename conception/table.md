
# Modèle
- id
- designation

# Avion
- id
- id_modèle 
- nb siège business
- nb siège eco 
- date fabrication

# Type_siege
- id
- designation


# Ville
- id
- designation


# Vol 
- id
- date heure depart
- date heure arrivee
- id avion
- id ville
- prix business
- prix eco

# Promotion_siege
- id
- pourcent
- id_vol
- id_type_siege
- nb siege

# Regle_reservation
- id
- id_vol
- nb_heure_limite avant vol

# Regle_annulation_reservation
- id
- id_vol
- nb_heure_limite avant vol

# Utilisateur
- id
- mail
- mdp

(# Reservation
- id
- id_user
- date heure reservation
- id_vol
- id_type_siege
- montant
)


# Reservation
- id
- id_user (table utilisateur)
- date heure reservation
- nb_adulte
- nb_enfant
- id_vol (table vol)
- montant
- complete

# Detail_reservation
- id
- id_reservation (table reservation)
- id_type_siege (table type_siege)
- nom
- prenom
- date_naissance
- passeport


# Vue pour connaitre le nb de place restant par vol par siege 