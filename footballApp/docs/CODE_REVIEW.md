# Code Review

Ovaj dokument je fokusiran na:

- konkretne bugove
- rizike u ponasanju aplikacije
- mesta gde implementacija ne odgovara nazivu metode ili endpointa
- preporuceni redosled ispravki

Prioritet je dat ovim redom:

- `P0` kriticno, direktno daje netacno ponasanje ili visok rizik pucanja
- `P1` znacajan funkcionalni problem
- `P2` tehnicki dug ili nekonzistentnost koja lako prerasta u bug

## P0

### 1. Pogresno azuriranje statistike zutih kartona

Fajl:

- `src/main/java/com/fifa/footballApp/service/MatchEventService.java`

Problem:

Kod za zuti karton koristi pogresan brojac:

- `referee.setTotalYellowCardsGiven(referee.getTotalRedCardsGiven() + 1);`

To znaci da se broj zutih kartona racuna iz broja crvenih kartona umesto iz broja zutih kartona.

Posledica:

- statistika sudije postaje netacna
- svaki novi zuti karton moze da “preskace” ili resetuje realnu vrednost

Preporuka:

- promeniti na:
  `referee.setTotalYellowCardsGiven(referee.getTotalYellowCardsGiven() + 1);`

### 2. “Utakmice tima” se trenutno ne pretrazuju po timu

Fajlovi:

- `src/main/java/com/fifa/footballApp/service/TeamService.java`
- `src/main/java/com/fifa/footballApp/repository/MatchRepo.java`

Problem:

Metode:

- `getPastMatches(String id)`
- `getUpcomingMatches(String id)`

pozivaju:

- `matchRepo.findByIdAndDateBefore(id, ...)`
- `matchRepo.findByIdAndDateAfter(id, ...)`

Ovde se `id` tumaci kao `Match.id`, a ne kao `Team.id`.

Posledica:

- endpointi `/api/v1/teams/{id}/past-matches` i `/api/v1/teams/{id}/upcoming-matches` ne vracaju stvarno utakmice tima
- naziv API-ja i ponasanje su u konfliktu

Preporuka:

- uvesti repozitorijumske metode koje filtriraju po `homeTeam.id` ili `awayTeam.id` i po datumu

## P1

### 3. Endpoint postoji, ali logika statistike igraca nije implementirana

Fajl:

- `src/main/java/com/fifa/footballApp/service/PlayerService.java`

Problem:

- `getPlayerStatistics(String id)` vraca `null`

Posledica:

- endpoint `GET /api/v1/players/{id}/statistics` vraca neupotrebljiv odgovor ili moze dovesti do neocekivanog ponasanja na klijentu

Napomena:

- paralelno postoji `GET /api/v1/players/{id}/stats` koji jeste implementiran

Preporuka:

- ili ukloniti/zakljucati `/{id}/statistics`
- ili ga delegirati na `getPlayerStats()`

### 4. Search endpoint za sudije postoji, ali servis vraca `null`

Fajl:

- `src/main/java/com/fifa/footballApp/service/RefereeService.java`

Problem:

- `searchReferees(String nationality, String name, Integer age)` vraca `null`

Posledica:

- `GET /api/v1/referees/search` formalno postoji, ali funkcionalno nije zavrsen
- klijent mora da radi dodatne null provere ili dobija prazan/neispravan rezultat

Preporuka:

- povezati ga sa vec postojecim `refereeRepo.findByQuery(...)`

### 5. Uklanjanje igraca iz tima je u konfliktu sa JPA mapiranjem

Fajlovi:

- `src/main/java/com/fifa/footballApp/service/TeamService.java`
- `src/main/java/com/fifa/footballApp/model/Player.java`

Problem:

`removePlayerFromTeam()` radi:

- `player.setTeam(null);`

Ali u `Player` entitetu relacija je definisana kao:

- `@JoinColumn(..., nullable = false)`

Posledica:

- pri realnom pozivu moze doci do persistence greske ili pucanja transakcije

Preporuka:

- ili dozvoliti `nullable = true`
- ili promeniti poslovno pravilo tako da igrac uvek mora pripadati nekom timu

### 6. Uklanjanje coach-a sa tima ignorise `coachId` iz putanje

Fajl:

- `src/main/java/com/fifa/footballApp/service/TeamService.java`

Problem:

`removeCoachFromTeam(String teamId, String coachId)` uopste ne proverava da li prosledjeni `coachId` odgovara coach-u koji je trenutno dodeljen timu.

Posledica:

- bilo koji `coachId` u URL-u ima isti efekat
- API semantika je varljiva

Preporuka:

- proveriti da li je `team.getCoach()` isti coach
- ili ukloniti `coachId` iz endpointa ako nije potreban

### 7. Uklanjanje igraca iz tima ignorise `teamId`

Fajl:

- `src/main/java/com/fifa/footballApp/service/TeamService.java`

Problem:

`removePlayerFromTeam(String teamId, String playerId)` ne proverava da li igrac zaista pripada prosledjenom timu.

Posledica:

- endpoint semanticki tvrdi “ukloni igraca iz ovog tima”, ali zapravo samo cisti `player.team`
- to otvara prostor za nekonzistentne pozive i tesko pracenje bugova

Preporuka:

- pre brisanja proveriti `player.getTeam().getId().equals(teamId)`

## P2

### 8. Dva paralelna modela za “trenera tima”

Fajlovi:

- `src/main/java/com/fifa/footballApp/model/Team.java`
- `src/main/java/com/fifa/footballApp/model/CoachTeamEngagement.java`

Problem:

Trener tima se modeluje na dva nacina:

- direktno preko `Team.coach`
- indirektno preko `CoachTeamEngagement`

Posledica:

- lako dolazi do divergentnog stanja
- nije jasno koji model predstavlja “source of truth”

Primer:

- `Team.coach` moze pokazivati na jednog trenera
- aktivni `CoachTeamEngagement` moze pokazivati na drugog

Preporuka:

- izabrati jedan model kao primarni
- drugi svesti na derived ili istorijski zapis

### 9. Globalni exception handler pokriva samo jedan custom exception

Fajl:

- `src/main/java/com/fifa/footballApp/exception/GlobalExceptionHandler.java`

Problem:

Handler obradjuje samo `TeamNotFoundException`, dok vecina servisa baca `EntityNotFoundException`.

Posledica:

- response format za greske nije konzistentan kroz aplikaciju
- klijent dobija razlicite oblike gresaka za slicne situacije

Preporuka:

- centralno obraditi `EntityNotFoundException`
- eventualno standardizovati sve business exception tipove

### 10. `Team.defaultFormation` koristi ordinal mapiranje

Fajl:

- `src/main/java/com/fifa/footballApp/model/Team.java`

Problem:

`@Enumerated` bez `EnumType.STRING` koristi ordinal.

Posledica:

- promena redosleda enum vrednosti menja persisted podatke
- H2 testovi vec loguju upozorenja zbog DDL razlika

Preporuka:

- koristiti `@Enumerated(EnumType.STRING)`

### 11. `CompetitionRepository` koristi drugi tip ID-a od modela

Fajlovi:

- `src/main/java/com/fifa/footballApp/model/Competition.java`
- `src/main/java/com/fifa/footballApp/repository/CompetitionRepository.java`

Problem:

- entitet koristi `String competitionId`
- repo je deklarisan kao `JpaRepository<Competition, UUID>`

Posledica:

- repository tip nije uskladjen sa entitetom
- to je skriven izvor buducih gresaka cim se repo ozbiljnije koristi

Preporuka:

- uskladiti repo sa stvarnim tipom ID-a

### 12. CORS konfiguracija je hardkodirana i implementirana kao filter

Fajl:

- `src/main/java/com/fifa/footballApp/config/WebConfig.java`

Problem:

- origin je hardkodiran na `http://localhost:3000`
- konfiguracija nije profilisana

Posledica:

- deployment van local okruzenja zahteva kodnu izmenu

Preporuka:

- prebaciti origin u konfiguraciju
- po zelji koristiti standardni `WebMvcConfigurer`

### 13. `ddl-auto=create-drop` je preagresivan za lokalni profil

Fajl:

- `src/main/resources/application.yaml`

Problem:

- pri svakom startu se sema ponovo pravi
- pri svakom gasenju se brise

Posledica:

- podaci nisu trajni na nivou seme
- lokalno testiranje i debugging trpe ako se ocekuje stabilna baza

Preporuka:

- za local profil razmotriti `update`
- za test ostaviti `create-drop`

### 14. Kontroleri direktno vracaju JPA entitete

Problem:

- response format je cvrsto vezan za persistence model
- lazy relacije i Jackson anotacije postaju deo API dizajna

Posledica:

- svaka promena entiteta lako postaje API breaking change

Preporuka:

- postepeno uvoditi response DTO-jeve za spoljne odgovore

## Redosled ispravki

Najprakticniji redosled:

1. ispraviti `MatchEventService` za zute kartone
2. srediti `TeamService` + `MatchRepo` da utakmice tima rade ispravno
3. implementirati `PlayerService.getPlayerStatistics()`
4. implementirati `RefereeService.searchReferees()`
5. resiti konflikt oko `removePlayerFromTeam()` i `nullable = false`
6. dodati proveru konzistentnosti za remove/assign operacije
7. uskladiti `CompetitionRepository` ID tip
8. standardizovati exceptions i API greske
9. odluciti da li je `Team.coach` ili `CoachTeamEngagement` primarni model
10. srediti enum mapiranja i CORS konfiguraciju

## Kratak zakljucak

Kod je dovoljno dobar da se aplikacija podigne, build prodje i osnovni CRUD tokovi rade, ali postoji nekoliko mesta gde:

- ime metode i stvarno ponasanje nisu uskladjeni
- deo endpointa nije zavrsen
- deo statistike daje netacne podatke

Drugim recima:

- baza i skeleton aplikacije postoje
- domen je solidno zamisljen
- ali servisni sloj jos nije potpuno zatvoren i siguran za dalje sirenje bez nekoliko ciljnih ispravki
