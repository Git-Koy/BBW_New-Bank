# UC-04 Kontoauszug erhalten

| Feld | Inhalt |
|---|---|
| **Name** | Kontoauszug erhalten |
| **Szenario** | Bank 2 – Kunde am Bankschalter ([`01_UML_Bank2.pdf`](../scenarios/01_UML_Bank2.pdf)), Buchungen ([`03_UML_Bank3.pdf`](../scenarios/03_UML_Bank3.pdf)) |
| **Sprint** | 2 – Buchungen |

## Kurzbeschreibung

Eine Kundin oder ein Kunde verlangt einen Kontoauszug. Der Auszug listet alle Buchungen mit Datum,
Betrag und Text auf und zeigt am Schluss den Saldo. Die Beträge sind lesbar in CHF formatiert.

## Akteure

- **Kunde / Kundin** (primärer Akteur): will wissen, was auf dem Konto passiert ist.
- **Bank** (System): sucht das Konto.

## Auslöser

Die Kundin oder der Kunde verlangt am Bankschalter einen Kontoauszug für eine Kontonummer.

## Ergebnisse

Ein Text mit allen Buchungen und dem Saldo.

## Vorbedingung

Das Konto existiert (siehe [UC-01](UC-01-konto-eroeffnen.md)).

## Nachbedingung

Das Konto ist unverändert; ein Auszug ändert keine Buchung.

## Eingehende Daten

| Daten | Typ | Regel |
|---|---|---|
| Kontonummer | `String` | muss in der Bank existieren |

## Essenzieller Ablauf

1. Der Kunde nennt die Kontonummer.
2. Die Bank sucht das Konto (`getAccount(id)`).
3. Das Konto erstellt den Auszug (`printStatement()`): pro Buchung Datum, Betrag und Text.
4. Das Konto hängt den Saldo an, formatiert mit `MoneyFormatter.toCHF(...)` (z. B. `CHF 0.00002`).
5. Der Kunde erhält den Auszug.

### Alternativabläufe

- **2a. Unbekannte Kontonummer:** Die Bank lehnt mit `InvalidAmountException` ab.
- **3a. Konto ohne Buchungen:** Der Auszug enthält nur den Saldo `CHF 0.00000`.

## Offene Punkte

- Ein Auszug nur für einen Monat (`print(year, month)`) ist noch nicht umgesetzt; der Test
  `testMonthlyPrint` gibt nur den aktuellen Saldo aus.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-08 | Cedric Caviezel | Erste Version (Sprint 2) |

## Sonstiges

Tests: `AccountTests.testPrint`, `AccountTests.testFormatCurrency`, `BankTests.testPrint`,
`AccountTests.testAccountCannotBeModifiedbyGetBookings`.

## Anmerkungen

`getBookings()` gibt eine unveränderliche Kopie zurück, damit niemand über den Auszug Buchungen
ändern kann.
