## Game Microservice 
8081, verwaltet Spielstatus, Spielstart/-ende, zentraler Service zur Spielverwaltung

POST /games (erstellt neues Spiel)

POST /games/{id}/finish  (beendet das Spiel)

POST /games/{id}/check-start (prüft ob das Spiel starten kann)

GET /games/{id}/status (gibt zurück, ob das Spiel beendet ist)

GET /games/{id}/can-join (prüft, ob Spieler beitreten dürfen) 


