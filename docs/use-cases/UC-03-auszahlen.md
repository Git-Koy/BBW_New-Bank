# UC-03 Auszahlen

| Feld | Inhalt |
|---|---|
| **Name** | Auszahlen |
| **Szenario** | Bank 2 – Kunde am Bankschalter ([`01_UML_Bank2.pdf`](../scenarios/01_UML_Bank2.pdf)), Kontotypen ([`04_UML_Bank4b.pdf`](../scenarios/04_UML_Bank4b.pdf)) |
| **Sprint** | 1 – Baseline, 2 – Buchungen, 3 – Kontotypen |

## Kurzbeschreibung

Eine Kundin oder ein Kunde hebt am Bankschalter einen Betrag von einem bestehenden Konto ab. Jeder
Kontotyp hat seine eigene Regel, wie weit abgehoben werden darf. Die Bank kennt diese Regeln nicht,
sie ruft nur `withdraw(...)` auf dem Konto auf (Polymorphie).

## Akteure

- **Kunde / Kundin** (primärer Akteur): will Geld abheben.
- **Bank** (System): sucht das Konto und gibt die Auszahlung weiter.

## Auslöser

Die Kundin oder der Kunde nennt am Bankschalter eine Kontonummer, einen Betrag und optional einen
Buchungstext.

## Ergebnisse

- Das Konto hat eine Buchung mehr, mit negativem Betrag.
- Der Saldo des Kontos ist um den Betrag kleiner.

## Vorbedingung

- Das Konto existiert (siehe [UC-01](UC-01-konto-eroeffnen.md)).
- Der Betrag ist grösser als 0 und das Datum liegt nicht in der Vergangenheit.
- Die Regel des Kontotyps erlaubt den Betrag:

| Kontotyp | Regel |
|---|---|
| Sparkonto | Saldo darf nicht unter 0 fallen |
| Promo-Jugendsparkonto | wie Sparkonto, zusätzlich höchstens CHF 200 pro Monat |
| Lohnkonto | Saldo darf bis zur Kreditlimite fallen, nicht tiefer |

## Nachbedingung

- **Erfolg:** Saldo neu = Saldo alt − Betrag. Die Auszahlung ist als **negative** Buchung
  gespeichert.
- **Fehler:** Saldo und Buchungen sind unverändert.

## Eingehende Daten

| Daten | Typ | Regel |
|---|---|---|
| Kontonummer | `String` | muss in der Bank existieren |
| Datum | `Scheduled` | darf nicht in der Vergangenheit liegen |
| Betrag | `long` (Millirappen) | grösser als 0, und die Regel des Kontotyps muss ihn erlauben |
| Text | `String` | optional; ohne Text heisst die Buchung «Auszahlung» |

## Essenzieller Ablauf

1. Der Kunde nennt Kontonummer, Datum, Betrag und Text.
2. Die Bank sucht das Konto mit dieser Kontonummer.
3. Das Konto prüft die Regel seines Typs (siehe Vorbedingung).
4. Das Konto prüft, dass der Betrag grösser als 0 ist und das Datum nicht in der Vergangenheit liegt.
5. Das Konto speichert eine Buchung mit negativem Betrag.
6. Das Konto gibt den neuen Saldo zurück.

### Alternativabläufe

- **2a. Unbekannte Kontonummer:** Die Bank lehnt mit `InvalidAmountException` ab.
- **3a. Sparkonto, Saldo reicht nicht:** Abgelehnt mit `InvalidAmountException`. Genau den Saldo
  abheben ist erlaubt; der Saldo ist danach 0.
- **3b. Promo-Jugendsparkonto, Monatslimite überschritten:** Abgelehnt mit
  `InvalidAmountException`. Im nächsten Monat beginnt die Limite neu.
- **3c. Lohnkonto, Kreditlimite überschritten:** Abgelehnt mit `InvalidAmountException`. Genau bis
  zur Kreditlimite ist erlaubt.
- **4a. Betrag ist 0 oder negativ:** Abgelehnt mit `InvalidAmountException`.
- **4b. Datum liegt in der Vergangenheit:** Abgelehnt mit `InvalidDateException`.

## Offene Punkte

- Unbekannte Kontonummer, zu kleiner Saldo und überschrittene Limite melden alle
  `InvalidAmountException`; eigene Exceptions wären genauer.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-02 | Cedric Caviezel | Erste Version (Sprint 1) |
| 2026-11-08 | Cedric Caviezel | Buchungstext, Regeln von Lohnkonto und Promo-Jugendsparkonto (Sprint 2–3) |

## Sonstiges

Tests: `AccountTests.testWithdraw`, `SavingsAccountTests.*`, `SalaryAccountTests.*`,
`PromoYouthSavingsAccountTests.*`, `BankTests.testWithdraw`.

## Anmerkungen

Beträge sind in Millirappen (`100_000` = CHF 1.00). Auszahlungen werden als negative Beträge
gebucht.
