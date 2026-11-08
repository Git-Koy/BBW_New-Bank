# UC-02 Einzahlen

| Feld | Inhalt |
|---|---|
| **Name** | Einzahlen |
| **Szenario** | Bank 2 – Kunde am Bankschalter ([`01_UML_Bank2.pdf`](../scenarios/01_UML_Bank2.pdf)), Buchungen ([`03_UML_Bank3.pdf`](../scenarios/03_UML_Bank3.pdf)) |
| **Sprint** | 1 – Baseline, 2 – Buchungen |

## Kurzbeschreibung

Eine Kundin oder ein Kunde zahlt am Bankschalter einen Betrag auf ein bestehendes Konto ein. Das
Konto speichert eine Buchung mit Datum, Betrag und Text. Der Saldo steigt um den Betrag, weil er
aus den Buchungen berechnet wird.

## Akteure

- **Kunde / Kundin** (primärer Akteur): will Geld einzahlen.
- **Bank** (System): sucht das Konto und gibt die Einzahlung weiter.

## Auslöser

Die Kundin oder der Kunde nennt am Bankschalter eine Kontonummer, einen Betrag und optional einen
Buchungstext.

## Ergebnisse

- Das Konto hat eine Buchung mehr, mit positivem Betrag.
- Der Saldo des Kontos ist um den Betrag grösser.

## Vorbedingung

- Das Konto existiert (siehe [UC-01](UC-01-konto-eroeffnen.md)).
- Der Betrag ist grösser als 0.
- Das Transaktionsdatum liegt nicht in der Vergangenheit.

## Nachbedingung

- **Erfolg:** Saldo neu = Saldo alt + Betrag. Die Buchung steht in `getBookings()`.
- **Fehler:** Saldo und Buchungen sind unverändert.

## Eingehende Daten

| Daten | Typ | Regel |
|---|---|---|
| Kontonummer | `String` | muss in der Bank existieren |
| Datum | `Scheduled` | darf nicht in der Vergangenheit liegen (heute und Zukunft sind gültig) |
| Betrag | `long` (Millirappen) | muss grösser als 0 sein |
| Text | `String` | optional; ohne Text heisst die Buchung «Einzahlung» |

## Essenzieller Ablauf

1. Der Kunde nennt Kontonummer, Datum, Betrag und Text.
2. Die Bank sucht das Konto mit dieser Kontonummer.
3. Das Konto prüft, dass der Betrag grösser als 0 ist.
4. Das Konto prüft, dass das Datum nicht in der Vergangenheit liegt.
5. Das Konto speichert eine Buchung (Datum in Millisekunden seit 1970, Betrag, Text).
6. Das Konto gibt den neuen Saldo zurück (Summe aller Buchungen).

### Alternativabläufe

- **2a. Unbekannte Kontonummer:** Die Bank lehnt mit `InvalidAmountException` ab.
- **3a. Betrag ist 0 oder negativ:** Abgelehnt mit `InvalidAmountException`.
- **4a. Datum liegt in der Vergangenheit:** Abgelehnt mit `InvalidDateException`.

## Offene Punkte

- Eine unbekannte Kontonummer meldet die Bank mit `InvalidAmountException`. Eine eigene Exception
  wäre genauer.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-02 | Cedric Caviezel | Erste Version (Sprint 1) |
| 2026-11-08 | Cedric Caviezel | Buchungstext, Saldo aus Buchungen (Sprint 2) |

## Sonstiges

Tests: `AccountTests.testDeposit`, `AccountTests.testRejectedOperationsLeavingTheBalanceAndBookingsUnchanged`,
`AccountTests.testCanTransact`, `BankTests.testDeposit`, `BankTests.testBalance`,
`BookingTests.testEmptyOrBlankBookings`.

## Anmerkungen

Beträge sind in Millirappen (`100_000` = CHF 1.00). Jede Einzahlung wird als positive Buchung
gespeichert.
