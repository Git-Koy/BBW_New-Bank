# UC-05 Konten nach Saldo auflisten

| Feld | Inhalt |
|---|---|
| **Name** | Konten nach Saldo auflisten |
| **Szenario** | Bank 5 – Bankangestellte sortieren Konten ([`05_UML_Bank5.pdf`](../scenarios/05_UML_Bank5.pdf)) |
| **Sprint** | extra – Auswertung |

## Kurzbeschreibung

Eine Bankangestellte oder ein Bankangestellter will die fünf Konten mit dem höchsten oder dem
tiefsten Saldo sehen.

## Akteure

- **Bankangestellte/r** (primärer Akteur): will eine Auswertung der Konten.
- **Bank** (System): sortiert die Konten nach Saldo.

## Auslöser

Die Bankangestellte oder der Bankangestellte verlangt die Top-5- oder Bottom-5-Liste.

## Ergebnisse

Eine Liste mit höchstens fünf Konten, sortiert nach Saldo.

## Vorbedingung

Die Bank existiert. Sie darf auch weniger als fünf Konten haben.

## Nachbedingung

Die Konten sind unverändert; die Liste ist nur eine Auswertung.

## Eingehende Daten

Keine.

## Essenzieller Ablauf

1. Die Bankangestellte verlangt die Top-5-Liste (`top5HighestBalances()`) oder die
   Bottom-5-Liste (`top5LowestBalances()`).
2. Die Bank sortiert alle Konten nach Saldo, absteigend für Top 5, aufsteigend für Bottom 5.
3. Die Bank gibt die ersten fünf Konten zurück.

### Alternativabläufe

- **3a. Weniger als fünf Konten:** Die Liste enthält alle Konten.

## Offene Punkte

- Konten mit gleichem Saldo haben keine feste Reihenfolge untereinander.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-08 | Cedric Caviezel | Erste Version |

## Sonstiges

Tests: `BankTests.testTop5`, `BankTests.testBottom5`.

## Anmerkungen

Die Bank vergleicht nur `Account.getBalance()` und kennt den Kontotyp nicht.
