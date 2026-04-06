# footballApp

Spring Boot aplikacija za rad sa timovima, igracima, trenerima, sudijama i mecevima.

## Zahtevi

- Docker Desktop
- Java 17+ dostupna u terminalu

Napomena:
- Projekat je deklarisan za Java 17, ali je lokalno potvrdjen build i sa novijim JDK uz trenutno podeseni `pom.xml`.

## Lokalna konfiguracija

Napraviti lokalni `.env` na osnovu templejta:

```bash
cp .env.example .env
```

`docker compose` ce automatski procitati `.env`.

Za pokretanje aplikacije iz terminala, ucitaj promenljive iz `.env`:

```bash
set -a
source .env
set +a
```

## Pokretanje baze

Iz root-a projekta:

```bash
docker compose up -d
docker compose ps
```

PostgreSQL se podize sa vrednostima iz `.env`.

- host: `localhost`
- port: `5432`
- database: `football_app`
- username: `postgres`
- password: `nikola`

## Build

```bash
sh mvnw install
```

Ako hoces samo paket bez testova:

```bash
sh mvnw -DskipTests install
```

## Pokretanje aplikacije

Ako vec nisi, prvo ucitaj `.env` promenljive:

```bash
set -a
source .env
set +a
```

```bash
sh mvnw spring-boot:run
```

Aplikacija se dize na:

- `http://localhost:8082`

## Osnovne rute

- `GET /api/v1/teams`
- `GET /api/v1/players`
- `GET /api/v1/coach`
- `GET /api/v1/referees`
- `GET /api/v1/matches`
- `GET /api/v1/match-events`
- `GET /api/v1/coach-engagements`

## Testovi

Testovi koriste H2 `test` profil i ne zavise od lokalnog PostgreSQL-a:

```bash
sh mvnw test
```

## Gasenje

Za gasenje samo baze:

```bash
docker compose down
```

Ako hoces i da obrises volume sa podacima:

```bash
docker compose down -v
```
