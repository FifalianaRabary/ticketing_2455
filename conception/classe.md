# Avion
- id
- modèle 
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

# Reservation
- id
- id_user
- date heure reservation
- id_vol
- id_type_siege
- montant

