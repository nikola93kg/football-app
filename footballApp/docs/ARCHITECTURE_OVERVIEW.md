# Architecture Overview

## 1. Sta je ova aplikacija

`footballApp` je Spring Boot REST aplikacija za upravljanje fudbalskim domenom:

- timovi
- igraci
- treneri
- sudije
- utakmice
- dogadjaji na utakmici
- angazmani trenera po timu

Stack:

- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL za lokalni rad
- H2 za test profil
- Maven
- Lombok

Glavna ulazna tacka je `FotballAppApplication`.

## 2. Kako se aplikacija podize

Prilikom starta aplikacije desava se sledece:

1. Spring Boot ucita `application.yaml`.
2. Podrazumevani profil je `local`.
3. Profil `local` cita konekciju za bazu iz environment promenljivih definisanih kroz `.env`.
4. Kreira se `DataSource`.
5. Hibernate skenira entitete iz `model` paketa.
6. Zbog `spring.jpa.hibernate.ddl-auto=create-drop`, sema se pravi pri startu i brise pri gasenju aplikacije.
7. Pokusava se izvrsavanje `data.sql`.
8. Podize se embedded Tomcat na portu `8082`.
9. Aktiviraju se REST kontroleri i aplikacija postaje dostupna.

Test profil radi slicno, ali:

- koristi H2 in-memory bazu
- ne zavisi od lokalnog PostgreSQL-a

## 3. Struktura projekta

Paketi imaju standardnu Spring podelu:

- `config`
  odgovoran za infrastructuru, trenutno samo CORS filter
- `controller`
  REST endpointi
- `service`
  poslovna logika
- `repository`
  pristup bazi preko Spring Data JPA
- `model`
  JPA entiteti
- `dto`
  pomocni objekti za specijalne operacije
- `enums`
  domen-specific vrednosti
- `exception`
  globalni error handling

## 4. Domenski model

### Team

Centralni entitet aplikacije.

Polja:

- `id`
- `name`
- `logo`
- `competitions`
- `listOfPlayers`
- `coach`
- `defaultFormation`

Relacije:

- `ManyToMany` sa `Competition`
- `OneToMany` sa `Player`
- `OneToOne` sa `Coach`

Napomena:
- `defaultFormation` nema eksplicitno `EnumType.STRING`, pa se trenutno mapira ordinalno.

### Player

Predstavlja igraca koji pripada jednom timu.

Polja:

- `id`
- `name`
- `defaultPosition`
- `age`
- `nationality`
- `jerseyNumber`
- `transferFee`
- `transferDate`
- `positions`
- `team`

Relacije:

- `ManyToOne` prema `Team`
- kolekcija enum pozicija kroz posebnu tabelu `player_position`

### Coach

Jednostavan entitet sa osnovnim informacijama o treneru.

Polja:

- `id`
- `name`
- `age`
- `nationality`
- `trophiesWon`

### Referee

Sudija sa osnovnim i kumulativnim statistikama.

Polja:

- `id`
- `name`
- `age`
- `nationality`
- `totalYellowCardsGiven`
- `totalRedCardsGiven`

### Match

Modeluje utakmicu.

Polja:

- `id`
- `date`
- `time`
- `location`
- `stadium`
- `score`
- `homeTeam`
- `awayTeam`
- `referee`
- `events`
- `homeTeamFormation`
- `awayTeamFormation`

Relacije:

- `ManyToOne` prema `Team` za domacina i gosta
- `OneToOne` prema `Referee`
- `OneToMany` prema `MatchEvent`

### MatchEvent

Najbitniji entitet za statistike.

Predstavlja pojedinacan dogadjaj na utakmici.

Polja:

- `id`
- `match`
- `player`
- `team`
- `referee`
- `eventType`
- `eventTime`

Na osnovu ovih dogadjaja aplikacija racuna igracke i sudijske statistike.

### CoachTeamEngagement

Modeluje angazman trenera u timu.

Polja:

- `id`
- `team`
- `coach`
- `role`
- `startDate`
- `endDate`
- `isActive`

Ovo je odvojeno od `Team.coach`, sto znaci da aplikacija trenutno ima:

- jedan direktni “trenutni” coach na timu
- odvojen istorijski ili organizacioni zapis angazmana

To su dva razlicita mehanizma koja koegzistiraju.

### Competition

Modeluje takmicenje u kome timovi mogu da ucestvuju.

Polja:

- `competitionId`
- `name`
- `currentWinner`
- `prizeAmount`
- `description`
- `participants`

## 5. DTO objekti

### PlayerStats

Koristi se kao agregatni odgovor za statistiku igraca.

Sadrzi:

- golove
- asistencije
- suteve
- slobodne udarce
- ofsajde
- penale
- kartone
- izmene

### TransferDetails

Koristi se kod transfera igraca.

Sadrzi:

- `transferFee`
- `transferDate`

## 6. Repozitorijumi

Repozitorijumi su tanki sloj nad JPA i uglavnom se oslanjaju na Spring Data naming konvencije.

Primeri:

- `TeamRepo.findByNameContaining`
- `PlayerRepo.findByNameContainingAndNationality`
- `MatchEventRepo.findByPlayerId`
- `CoachTeamEngagementRepo.findByIsActiveTrue`

Posebno:

- `RefereeRepo` ima jedan `@Query` za kombinovanu pretragu po nacionalnosti, imenu i godinama

## 7. Poslovna logika po servisu

### TeamService

Odgovoran za:

- listanje i citanje timova
- kreiranje i update osnovnih podataka tima
- brisanje tima
- pretragu po imenu
- citanje igraca tima
- citanje coach-a tima
- dodelu i uklanjanje igraca iz tima
- dodelu i uklanjanje coach-a iz tima
- dohvatanje proslih i buducih utakmica

Bitno ponasanje:

- pri kreiranju proverava da li vec postoji tim sa istim imenom
- `getAllTeams()` vraca sortirano po `id`

Trenutni problem u logici:

- `getPastMatches()` i `getUpcomingMatches()` pozivaju `matchRepo.findByIdAndDateBefore/After(id, ...)`
- to filtrira po `Match.id`, a ne po `homeTeam` ili `awayTeam`
- prakticno ime metode sugerise “utakmice tima”, ali trenutna implementacija ne radi to kako naziv kaze

### PlayerService

Odgovoran za:

- CRUD nad igracima
- pretragu igraca po imenu i nacionalnosti
- transfer igraca u drugi tim
- racunanje statistike igraca iz `MatchEvent` zapisa
- listu utakmica na kojima je igrac imao dogadjaje

Kako radi statistika igraca:

1. ucitaju se svi `MatchEvent` zapisi za igraca
2. stream filtriranjem se broje dogadjaji po tipu
3. rezultat se pakuje u `PlayerStats`

Bitno:

- `getPlayerStatistics()` trenutno vraca `null`
- stvarna implementirana metoda za statistiku je `getPlayerStats()`

### CoachService

Odgovoran za:

- CRUD nad trenerima
- pretragu po imenu i/ili nacionalnosti

Logika je jednostavna i bez dodatnih side-effectova.

### RefereeService

Odgovoran za:

- CRUD nad sudijama
- racunanje statistike sudije iz `MatchEvent` zapisa

Kako radi statistika sudije:

1. ucitaju se svi `MatchEvent` zapisi za sudiju
2. iz njih se izvuku jedinstvene utakmice
3. prebroje se zuti i crveni kartoni
4. vrati se `Map<String, Long>`

Trenutni problem u logici:

- `searchReferees()` trenutno vraca `null`
- iako repo vec ima spreman `findByQuery(...)`

### MatchService

Odgovoran za:

- CRUD nad utakmicama
- pretragu po stadionu
- dohvatanje svih dogadjaja jedne utakmice

Ovaj servis nema dodatne validacije izmedju timova, sudije i meca.

### MatchEventService

Odgovoran za:

- CRUD nad dogadjajima utakmice
- pretragu dogadjaja po igracu
- azuriranje statistike sudije kada se zabelezi karton

Bitan tok:

1. sacuva se `MatchEvent`
2. ako je `eventType` `YELLOW_CARD` ili `RED_CARD`
3. uzme se `Referee`
4. azuriraju se brojaci kartona na sudiji
5. sudija se snimi nazad u bazu

Trenutni problem u logici:

- za zuti karton se poziva:
  `referee.setTotalYellowCardsGiven(referee.getTotalRedCardsGiven() + 1)`
- to je gotovo sigurno greska, jer koristi broj crvenih kartona za update zutih

### CoachTeamEngagementService

Odgovoran za:

- CRUD nad angazmanima trenera
- vracanje samo aktivnih angazmana

Ovo je najcistiji servis posle `CoachService`.

## 8. REST API sloj

Kontroleri uglavnom samo prosledjuju pozive servisima.

### Timovi

Base path: `/api/v1/teams`

Glavne operacije:

- `GET /api/v1/teams`
- `GET /api/v1/teams/{id}`
- `POST /api/v1/teams`
- `PUT /api/v1/teams/{id}`
- `DELETE /api/v1/teams/{id}`
- `GET /api/v1/teams/search?name=...`
- `GET /api/v1/teams/{id}/players-of-the-team`
- `GET /api/v1/teams/{id}/coach`
- `GET /api/v1/teams/{id}/past-matches`
- `GET /api/v1/teams/{id}/upcoming-matches`
- `POST /api/v1/teams/{teamId}/add-player/{playerId}`
- `DELETE /api/v1/teams/{teamId}/remove-player/{playerId}`
- `POST /api/v1/teams/{teamId}/assign-coach/{coachId}`
- `DELETE /api/v1/teams/{teamId}/remove-coach/{coachId}`

### Igraci

Base path: `/api/v1/players`

- `GET /api/v1/players`
- `GET /api/v1/players/{id}`
- `POST /api/v1/players`
- `PUT /api/v1/players/{id}`
- `DELETE /api/v1/players/{id}`
- `GET /api/v1/players/search?name=...&nationality=...`
- `GET /api/v1/players/{id}/statistics`
- `GET /api/v1/players/{id}/stats`
- `POST /api/v1/players/{id}/transfer?toTeamId=...`
- `GET /api/v1/players/{id}/matches`

Napomena:

- `/{id}/statistics` trenutno nema implementaciju
- `/{id}/stats` je funkcionalna varijanta

### Treneri

Base path: `/api/v1/coach`

- standardni CRUD
- pretraga po imenu i nacionalnosti

### Sudije

Base path: `/api/v1/referees`

- standardni CRUD
- `GET /api/v1/referees/search`
- `GET /api/v1/referees/{id}/statistics`

Napomena:

- endpoint za search postoji, ali servis trenutno ne vraca realne rezultate

### Utakmice

Base path: `/api/v1/matches`

- standardni CRUD
- pretraga po stadionu
- `GET /api/v1/matches/{id}/events`

### Dogadjaji utakmice

Base path: `/api/v1/match-events`

- listanje
- kreiranje
- update
- delete
- pretraga po `playerId`

### Angazmani trenera

Base path: `/api/v1/coach-engagements`

- standardni CRUD
- `GET /api/v1/coach-engagements/active`

## 9. Kako tipican request prolazi kroz aplikaciju

Primer: `GET /api/v1/players/{id}/stats`

1. Request dolazi u `PlayerController`.
2. Kontroler poziva `PlayerService.getPlayerStats(playerId)`.
3. Servis preko `MatchEventRepo.findByPlayerId(playerId)` cita sve evente igraca.
4. Nad eventima radi agregaciju po `MatchEventType`.
5. Formira `PlayerStats`.
6. Spring vraca JSON odgovor klijentu.

Primer: `POST /api/v1/players/{id}/transfer?toTeamId=...`

1. Kontroler prima `TransferDetails` iz body-ja.
2. Servis pronalazi igraca.
3. Servis pronalazi novi tim.
4. Menja `team`, `transferFee` i `transferDate`.
5. Snima igraca.

Primer: `POST /api/v1/match-events`

1. Kontroler prosledi `MatchEvent` servisu.
2. Servis ga snimi.
3. Ako je dogadjaj karton, pokusa da azurira statistiku sudije.
4. Rezultat vraca nazad kao JSON.

## 10. Error handling

Globalni handler trenutno eksplicitno pokriva samo `TeamNotFoundException`.

To znaci:

- za timove postoji lep `404` preko `ProblemDetail`
- za vecinu drugih `EntityNotFoundException` slucajeva nema centralizovanog custom handler-a
- oni se trenutno oslanjaju na Spring default obradu gresaka

## 11. CORS i web konfiguracija

`WebConfig` je implementiran kao `OncePerRequestFilter`.

Na svaki response dodaje:

- `Access-Control-Allow-Origin: http://localhost:3000`
- dozvoljene metode
- dozvoljene headere
- `Allow-Credentials: true`

To znaci da je aplikacija trenutno pripremljena za frontend koji radi lokalno na portu `3000`.

## 12. Konfiguracija i profili

### `application.yaml`

Drzi zajednicke postavke:

- port `8082`
- `ddl-auto=create-drop`
- default profil `local`

### `application-local.yaml`

Drzi datasource parametre kroz env promenljive:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### `application-test.yaml`

Drzi H2 in-memory konfiguraciju za testove.

## 13. Sta se trenutno “desava u pozadini”

Najkrace:

- aplikacija nema scheduler, messaging ni batch obradu
- sva logika je request-driven
- najveci “background” posao je Hibernate/JPA lifecycle:
  - mapiranje entiteta
  - generisanje seme
  - lazy loading relacija
  - SQL upiti preko repozitorijuma

Kada klijent pozove API:

- servis cita ili menja stanje u bazi
- neki servisi racunaju agregate u memoriji nad vracenim eventima
- ne postoji poseban cache, queue ili async obrada

## 14. Poznati problemi i tehnicki dug

Ovo je vazno, jer dokumentacija treba da opisuje realno stanje.

### Funkcionalni problemi

- `PlayerService.getPlayerStatistics()` nije implementiran
- `RefereeService.searchReferees()` nije implementiran
- `TeamService.getPastMatches()` i `getUpcomingMatches()` koriste pogresan kriterijum pretrage
- `MatchEventService.updateRefereeStatistics()` ima gresku pri update-u zutih kartona

### Arhitekturne nedoslednosti

- `Team.coach` i `CoachTeamEngagement` modeluju slicnu stvar na dva razlicita nacina
- deo update logike je veoma plitak i ne proverava konzistentnost odnosa
- neki delete scenariji mogu da budu problematicni zbog relacija i referencijalnog integriteta

### Persistence i test napomene

- H2 test profil prolazi, ali Hibernate i dalje loguje DDL upozorenja oko mapiranja `TeamFormation`
- razlog je sto `Team.defaultFormation` koristi ordinal mapiranje, koje H2 tretira drugacije od PostgreSQL-a
- build prolazi, ali log nije potpuno cist

### API / UX problemi

- naziv nekih endpointa ne odgovara stvarnoj implementaciji
- nema paginacije
- nema validacionih anotacija za request body-je
- nema autentikacije ni autorizacije

## 15. Preporuceni sledeci koraci

Ako se aplikacija dalje razvija, najvise smisla ima:

1. srediti neimplementirane metode i pogresne upite
2. standardizovati error handling za sve entitete
3. uskladiti model `coach` vs `coach engagement`
4. dodati validaciju ulaznih DTO/request objekata
5. uvesti jasne response DTO-jeve umesto direktnog vracanja JPA entiteta
6. srediti enum mapiranje da test log bude bez schema warning-a
7. napisati jos testova za servise i glavne API tokove

## 16. Kratak mentalni model aplikacije

Ako hoces najjednostavniju sliku:

- `Team` je centar
- `Player`, `Coach` i `Competition` se vezuju za tim
- `Match` vezuje dva tima i jednog sudiju
- `MatchEvent` belezi sta se desilo na utakmici
- statistike igraca i sudija ne cuvaju se unapred kao zaseban read model
- one se racunaju iz `MatchEvent` podataka kada ih API zatrazi

To je trenutno osnovni mehanizam po kome aplikacija radi.
