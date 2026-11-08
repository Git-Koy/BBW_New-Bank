# New Bank

Agiles Projekt im Modul **m320 Objektorientiertes Design** an der BBW. Wir bauen Schritt für Schritt
eine kleine Bank in Java: Kundinnen und Kunden eröffnen Konten, zahlen ein, heben ab und erhalten
einen Kontoauszug; Bankangestellte werten die Konten aus. In jedem Sprint kommt ein Teil dazu, immer
zuerst mit Tests (TDD) und danach mit der Umsetzung.

**Team:** Cedric Caviezel, Kay Ježek

---

## Schnellstart

**Voraussetzung:** JDK 21 oder neuer. Maven muss nicht installiert sein, das Projekt bringt den
Maven Wrapper (`mvnw`) mit.

```bash
# Code holen
git clone https://github.com/Git-Koy/BBW_New-Bank.git
cd BBW_New-Bank

# Alle Tests ausführen (alle grün)
./mvnw clean test

# Spring-Boot-Anwendung auf http://localhost:8080 starten
./mvnw spring-boot:run
```

Unter Windows `mvnw.cmd` statt `./mvnw` verwenden.

Ob das Backend läuft, zeigt der Health-Check von Spring Boot Actuator:

```bash
curl http://localhost:8080/actuator/health
```

Eine laufende Anwendung antwortet mit `"status":"UP"`.

### Frontend starten

Das React-Frontend braucht Node.js 18 oder neuer und das laufende Backend.

```bash
cd frontend
npm install
npm run dev     # http://localhost:5173
npm test        # Tests für das API-Modul und die CHF-Umrechnung
```

Die URL des Backends steht in `frontend/.env.development` (`VITE_API_URL`), nicht im Code.

---

## REST-API

Alle Beträge sind Millirappen. Das Datum ist optional (ISO-8601, z. B. `2026-12-01T10:00:00Z`);
ohne Datum gilt «jetzt». Fehler kommen als JSON `{ "error": "…" }` zurück.

| Methode | Pfad | Body | Antwort |
|---|---|---|---|
| POST | `/accounts/savings` | – | `201 { "id": "S-1000" }` |
| POST | `/accounts/promo-youth` | – | `201 { "id": "Y-1001" }` |
| POST | `/accounts/salary` | `{ "creditLimit": -50000 }` | `201 { "id": "P-1002" }` |
| GET | `/accounts/{id}` | – | `{ "id", "type", "balance" }` |
| GET | `/accounts/{id}/balance` | – | `{ "balance" }` |
| GET | `/accounts/{id}/bookings` | – | `[ { "date", "amount", "text" } ]` |
| POST | `/accounts/{id}/deposit` | `{ "amount", "date"?, "text"? }` | `{ "balance" }` |
| POST | `/accounts/{id}/withdraw` | `{ "amount", "date"?, "text"? }` | `{ "balance" }` |
| GET | `/bank/balance` | – | `{ "balance" }` |
| GET | `/bank/top5/highest` | – | `[ { "id", "type", "balance" } ]` |
| GET | `/bank/top5/lowest` | – | `[ { "id", "type", "balance" } ]` |

| Fehler | Status |
|---|---|
| Unbekannte Kontonummer (`AccountNotFoundException`) | `404` |
| Ungültiger Betrag, Saldo/Limite überschritten, Datum in der Vergangenheit, falsches Datum, positive Kreditlimite | `400` |

CORS erlaubt nur `http://localhost:5173` (Vite-Dev-Server).

---

## Domäne

| Begriff | Bedeutung |
|---|---|
| **Millirappen** | Einheit aller Beträge, immer als `long`. `100_000` Millirappen = CHF 1.00. `MoneyFormatter.toCHF(...)` macht daraus z. B. `CHF 1.00000` |
| **Buchung** (`Booking`) | Eine Transaktion mit Datum (Millisekunden seit 1970), Betrag und Text. Einzahlungen sind positiv, Auszahlungen **negativ**. Ohne Text heisst sie «Einzahlung» bzw. «Auszahlung» |
| **Saldo** | Summe aller Buchungen eines Kontos. Er wird nicht gespeichert, sondern immer aus den Buchungen berechnet |
| **Kontonummer** | Wird von der Bank vergeben: Präfix des Kontotyps und ein Zähler ab 1000, z. B. `S-1000` |
| **Transaktionsdatum** (`Scheduled`) | Darf nicht in der Vergangenheit liegen, sonst gibt es eine `InvalidDateException` |

### Kontotypen

| Kontotyp | Klasse | Präfix | Regel beim Abheben |
|---|---|---|---|
| Sparkonto | `SavingsAccount` | `S-` | darf nicht überzogen werden |
| Promo-Jugendsparkonto | `PromoYouthSavingsAccount` | `Y-` | wie Sparkonto, zusätzlich höchstens CHF 200 pro Monat |
| Lohnkonto | `SalaryAccount` | `P-` | darf bis zur Kreditlimite (0 oder negativ) überzogen werden |

Alle Kontotypen erben von der abstrakten Klasse `Account`. Die `Bank` kennt nur `Account` und
braucht kein `instanceof`; welcher Typ entsteht, entscheidet die `AccountFactory`.

---

## Aufbau

```
src/main/java/ch/bbw/
├── NewBankApplication.java      Einstiegspunkt der Spring-Boot-Anwendung
├── Bank.java                    Verwaltet die Konten (Singleton, Bank.getInstance())
├── AccountFactory.java          Erstellt Konten und vergibt die Kontonummern
├── AccountType.java             Kontotypen mit Präfix
├── Booking.java                 Eine Buchung (record)
├── Scheduled.java               Transaktionsdatum
├── MoneyFormatter.java          Millirappen als CHF-Betrag
├── accounts/                    Account, SavingsAccount, PromoYouthSavingsAccount, SalaryAccount
├── exceptions/                  InvalidAmountException, InvalidDateException, AccountNotFoundException
├── service/                     BankService (@Service), BankConfiguration (Bank als Spring-Bean)
└── web/                         AccountController, BankController, ErrorHandler, WebConfiguration (CORS)
    └── dto/                     Request- und Response-Records, die über HTTP gehen

src/test/java/                   JUnit-5-Tests, mindestens 5 pro Fachklasse, inkl. @SpringBootTest
frontend/                        React-Frontend (Vite): src/api.js ist das zentrale API-Modul
docs/
├── use-cases/                   Anwendungsfälle UC-01 bis UC-06 mit Diagramm
├── class-diagram.md             Klassendiagramm
├── architecture.md              Verantwortlichkeiten, Entwurfsentscheide, Sprints
├── exercises/                   Aufgabenstellungen der Sprints
└── scenarios/                   Szenarien als PDF
```

Mehr dazu: [Anwendungsfälle](docs/use-cases/README.md), [Klassendiagramm](docs/class-diagram.md),
[Architektur und Sprints](docs/architecture.md).

---

## Sprints

Jeder Sprint hat einen eigenen Branch. Die Tests werden vor der Umsetzung committet, fertige
Sprints kommen per Merge in `main`.

| Sprint | Thema | Branch | Ergebnis |
|---|---|---|---|
| 1 | Baseline | `main` | Konto eröffnen, ein- und auszahlen, Saldo |
| 2 | Buchungen | `02-Excercise-bookings` | Buchungstext, Saldo aus Buchungen, Kontoauszug |
| 3 | Kontotypen | `03-Exercise-specific-accounts` | Lohnkonto, Promo-Jugendsparkonto, Vererbung |
| 4 | Factory Pattern | `04-Exercise-factory-pattern` | richtige Kontotypen, `AccountType`, thread-sichere Nummern |
| 5 | Singleton Pattern | `05-Exercise-singleton-pattern` | eine Bank, Test-Isolation, Factory per Konstruktor |
| 6–11 | Spring-Boot-Backend | `06-Exercise-build-the-be` | REST-API, Service-Schicht, DTOs, Fehlerbehandlung, CORS |
| 13 | React-Frontend | `13-Exercise-frontend` | Konto eröffnen, ein- und auszahlen, Kontoauszug, Bankübersicht |

Die Aufgaben stehen in [`docs/exercises/`](docs/exercises/), die Szenarien in
[`docs/scenarios/`](docs/scenarios/).

---

## Technik

| | |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Build | Maven Wrapper |
| Tests | JUnit 5 (`spring-boot-starter-test`) |
| Monitoring | Spring Boot Actuator, Health-Check unter `/actuator/health` |
| Datenbank | H2 in-memory (noch nicht genutzt, die Bank lebt im Speicher) |
| Frontend | React 18, Vite 5, Tests mit dem Node-Test-Runner |

Die Domänenklassen (`Account`, `AccountFactory`, …) sind bewusst **keine** Spring-Beans, damit das
objektorientierte Design für sich steht. Nur die eine `Bank` wird in `BankConfiguration` als Bean
bereitgestellt; Controller → `BankService` → `Bank` bekommen ihre Abhängigkeiten per Konstruktor.
