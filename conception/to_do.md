# BACKOFFICE
- [X] login 

- [X] CRUD Vol:
    - [X] insertion (avec prix siege par classe)
    - [X] liste 
    - [X] suppression
    - [X] modification

- [X] recherche multicritère de vol : date depart, date arrivee, ville depart, ville arrivee, prix max , prix min (champ séparées pour siege economique et siège business) ,modèle avion, avion

- [X] règles de gestion
    - [X] Promotion : nb siege sous promotion, pourcentage ,par vol, par classe
    - [X] Reservation : 
        - [X] nb heure avant vol qui determine fin de prise de reservation
        - [X] nb heure avant vol qui determine fin de prise d'annulation de reservation

- [X] check level

# FRONTOFFICE
- [X] login
- [X] liste vol
- [X] recherche multicritère de vol : date depart, date arrivee, ville depart, ville arrivee, prix max, prix min, modèle, avion
- [] insertion reservation
    - [] ajout nb siege en promo restant dans la table PromotionSiege
    - [] estGuichetFermer(vol,type_siege) : return nb place dispo == 0
    - [] calculPrixReservation () : jerena raha misy promotion lay classe, jerena raha nbSiegeRestant > 0 => mahazo promotion si oui 
    - [] reservation 
