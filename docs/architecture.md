# Architektur und Sprints

## Aufbau

```
src/main/java/ch/bbw/
├── NewBankApplication.java      Einstiegspunkt der Spring-Boot-Anwendung
├── Bank.java                    Verwaltet die Konten (Singleton)
├── AccountFactory.java          Erstellt Konten und vergibt die Kontonummern
├── AccountType.java             Kontotypen mit ihrem Präfix (S, Y, P)
├── Booking.java                 Eine Buchung: Datum, Betrag, Text (record)
├── Scheduled.java               Transaktionsdatum, erkennt Daten in der Vergangenheit
├── MoneyFormatter.java          Formatiert Millirappen als CHF-Betrag
├── accounts/
│   ├── Account.java             abstrakt: Kontonummer, Buchungen, Saldo, Ein- und Auszahlen
│   ├── SavingsAccount.java      Sparkonto: darf nicht überzogen werden
│   ├── PromoYouthSavingsAccount.java  Sparkonto mit Monatslimite CHF 200
│   └── SalaryAccount.java       Lohnkonto: darf bis zur Kreditlimite überzogen werden
└── exceptions/
    ├── InvalidAmountException.java
    └── InvalidDateException.java
```

Das [Klassendiagramm](class-diagram.md) zeigt alle Klassen mit Attributen, Methoden und
Beziehungen, die [Anwendungsfälle](use-cases/README.md) zeigen, was die Bank für wen tut.

## Verantwortlichkeiten

Jede Klasse hat genau eine Aufgabe:

| Klasse | Verantwortung | Weiss nichts von |
|---|---|---|
| `Bank` | Konten speichern, finden und auswerten | konkreten Kontotypen |
| `AccountFactory` | entscheiden, welcher Kontotyp entsteht, und die Nummer vergeben | wo die Konten gespeichert werden |
| `AccountType` | Typ und Präfix zusammenhalten | — |
| `Account` und Unterklassen | Buchungen halten und die Regel des Kontotyps prüfen | anderen Konten, der Bank |
| `Booking` | eine einzelne Buchung festhalten | Regeln |
| `Scheduled` | ein Transaktionsdatum darstellen | Konten |
| `MoneyFormatter` | Beträge lesbar machen | Konten |

## Entwurfsentscheide

- **Millirappen als `long`:** Alle Beträge sind ganze Zahlen (`100_000` = CHF 1.00). So gibt es
  keine Rundungsfehler wie mit `double`.
- **Saldo aus den Buchungen:** `Account` hat kein Attribut `balance`. `getBalance()` ist die Summe
  aller Buchungen, damit es nur eine Quelle der Wahrheit gibt. Auszahlungen sind negative Buchungen.
- **Unveränderliche Buchungsliste:** `getBookings()` gibt eine Kopie zurück, die man nicht ändern
  kann.
- **Polymorphie statt `instanceof`:** Die Bank ruft nur Methoden von `Account` auf. Jeder Kontotyp
  setzt seine Regel in `withdraw(...)` und seinen Namen in `getAccountType()` selbst um.
- **Vererbung:** `PromoYouthSavingsAccount` erbt von `SavingsAccount`, weil es ein Sparkonto mit
  einer zusätzlichen Regel ist. `SalaryAccount` erbt direkt von `Account`, weil es überzogen werden
  darf und die Regel des Sparkontos das verbieten würde.
- **Simple Factory:** Die `AccountFactory` ist die einzige Stelle, die `new SavingsAccount(...)`
  usw. aufruft. Sie hat eine Methode `create(AccountType, long)` mit einem `switch`; die drei
  benannten Methoden (`createSavingsAccount()` …) rufen sie auf, damit die Bank keine sinnlose
  Kreditlimite für Sparkonten übergeben muss.
- **Kontonummern:** Ein gemeinsamer Zähler für alle Typen (`S-1000`, `Y-1001`, `P-1002`), als
  `AtomicLong`, damit zwei Threads nie dieselbe Nummer bekommen.
- **Singleton:** Es gibt nur eine Bank (`Bank.getInstance()`, Holder-Idiom: lazy und thread-safe
  ohne Kosten pro Aufruf). Der Vergleich mit eager und lazy-synchronized steht im JavaDoc von
  `getInstance()`, die Argumente dafür und dagegen im JavaDoc von `Bank`.
- **Composition Root:** Der Konstruktor der Bank bekommt die `AccountFactory` übergeben und ist
  package-private. Tests können so eine eigene Bank mit bekannten Kontonummern bauen;
  `getInstance()` soll nur am äussersten Einstiegspunkt verwendet werden.
- **`reset()` nur für Tests:** Weil alle Tests dieselbe Bank teilen, leert `BankTests` sie vor jedem
  Test. Das ist ein bewusster Workaround und kein gutes Design (siehe JavaDoc von `reset()`).

## Sprints

Jeder Sprint wird in einem eigenen Branch entwickelt. Zuerst werden die Tests committet (rot),
danach die Umsetzung (grün). Fertige Branches kommen per Merge in `main`.

| Sprint | Thema | Branch | Verantwortlich | Ergebnis |
|---|---|---|---|---|
| 1 | Baseline | `main` | Vorgabe | `Bank` und `Account`: Konto eröffnen, ein- und auszahlen |
| 2 | Buchungen | `02-Excercise-bookings` | Kay Ježek | Buchungstext, Saldo aus Buchungen, `getBookings()`, Kontoauszug |
| 3 | Kontotypen | `03-Exercise-specific-accounts` | Kay Ježek | `SalaryAccount`, `PromoYouthSavingsAccount`, abstrakte `getAccountType()` |
| 4 | Factory | `04-Exercise-factory-pattern` | Cedric Caviezel | richtige Kontotypen, `AccountType`, `create(...)`, thread-sichere Nummern |
| 5 | Singleton | `05-Exercise-singleton-pattern` | Cedric Caviezel | `getInstance()`, Test-Isolation mit `reset()`, Factory per Konstruktor |
| – | Dokumentation | `Documentation` | Cedric Caviezel | README, Anwendungsfälle, Klassendiagramm, Architektur, fehlende Tests |
| 6–11 | Spring-Boot-Backend | – | – | noch offen ([Aufgabe](exercises/10-Exercise-build-the-be.md)) |

## Tests

```bash
./mvnw clean test
```

Pro Fachklasse gibt es mindestens fünf Tests mit `assert`, inklusive Grenzfällen (genau den Saldo
abheben, genau bis zur Kreditlimite, Monatslimite, Datum in der Vergangenheit, Betrag 0).

| Testklasse | Testet |
|---|---|
| `BankTests` | Konten über die Bank eröffnen, ein- und auszahlen, Gesamtsaldo, Top 5, Isolation |
| `BankSingletonTests` | `getInstance()`, kein öffentlicher Konstruktor, injizierte Factory, `reset()` |
| `AccountTests` | Buchungen, Saldo, Kontoauszug, Datumsprüfung |
| `SavingsAccountTests` | kein Überziehen, ungültige Beträge und Daten |
| `SalaryAccountTests` | Kreditlimite, positive Limite wird abgelehnt |
| `PromoYouthSavingsAccountTests` | Monatslimite, nächster Monat, ist ein Sparkonto |
| `AccountFactoryTests` | richtige Typen, Präfixe, Kreditlimite, eindeutige Nummern auch mit Threads |
| `AccountTypeTests` | Präfixe, Eindeutigkeit |
| `BookingTests` | Werte, Standardtexte, negative Beträge |
