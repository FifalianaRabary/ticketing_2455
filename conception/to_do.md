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

- [X] restApi

# FRONTOFFICE
- [X] login
- [X] liste vol
- [X] recherche multicritère de vol : date depart, date arrivee, ville depart, ville arrivee, prix max, prix min, modèle, avion
- [X] insertion reservation
    - [X] vue Promotions_Disponibles
    - [X] estDansLesTemps reservation
    - [X] estGuichetFermer(vol,type_siege) : return nb place dispo == 0
    - [] calculPrixReservation () : jerena raha misy promotion lay classe, jerena raha nbSiegeRestant > 0 => mahazo promotion si oui 
    - [X] reservation 
- [Y] upload photo passeport


# SUITE 
## SPRING
- [X] parametrage differents tarifs pour enfant (age max) et adulte pour un vol : x% ihany no aloha pour un enfant


# SUITE
## WEB-SERVICE (standard misy json etc.)
- [] amlay liste reservation  misy lien télécharger pdf : pdf avec les info des passagers, vol, total 