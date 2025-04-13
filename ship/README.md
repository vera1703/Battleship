## Ship Microservice
8083, verwaltet Schiffe und deren Positionen, prüft Treffer, erkennt verlorene Spiele

POST /ships/{playerId} (plaziert Schiff aufs Spielfeld)

GET /ships/check-hit (prüft, ob Schuss Gegner getroffen hat)

GET /ships/opponent-defeated (prüft, ob Gegner alle Schiffe verloren hat) 
