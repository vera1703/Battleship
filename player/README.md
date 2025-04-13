## Player Microservice
8082,verwaltet Spieler, speichert deren Spielzugehörigkeit, findet Gegenspieler, plaziert Schüsse

POST /players/{gameId}/{playerName} (Spieler registrieren)

GET /players/{playerId}/gameId (liefert Game-Id eines Spielers)

GET /players/{playerId}/opponent (liefert Gegner-ID)

POST /guesses/{playerId} (Schuss/Guess abliefern)
