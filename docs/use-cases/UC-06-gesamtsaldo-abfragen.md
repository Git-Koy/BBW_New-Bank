# UC-06 Gesamtsaldo der Bank abfragen

| Feld | Inhalt |
|---|---|
| **Name** | Gesamtsaldo der Bank abfragen |
| **Szenario** | Bank 2 ([`01_UML_Bank2.pdf`](../scenarios/01_UML_Bank2.pdf)), Kontotypen ([`04_UML_Bank4b.pdf`](../scenarios/04_UML_Bank4b.pdf)) |
| **Sprint** | 1 – Baseline |

## Kurzbeschreibung

Eine Bankangestellte oder ein Bankangestellter will wissen, wie viel Geld insgesamt auf allen
Konten der Bank liegt. Die Bank zählt die Salden aller Konten zusammen, egal welcher Kontotyp.

## Akteure

- **Bankangestellte/r** (primärer Akteur): will den Gesamtsaldo kennen.
- **Bank** (System): summiert die Salden.

## Auslöser

Die Bankangestellte oder der Bankangestellte verlangt den Gesamtsaldo.

## Ergebnisse

Der Gesamtsaldo in Millirappen.

## Vorbedingung

Die Bank existiert.

## Nachbedingung

Die Konten sind unverändert.

## Eingehende Daten

Keine.

## Essenzieller Ablauf

1. Die Bankangestellte verlangt den Gesamtsaldo (`Bank.getBalance()`).
2. Die Bank fragt jedes Konto nach seinem Saldo.
3. Die Bank gibt die Summe zurück.

### Alternativabläufe

- **2a. Keine Konten:** Der Gesamtsaldo ist 0.
- **2b. Ein Lohnkonto ist im Minus:** Der negative Saldo wird mitgezählt und senkt den Gesamtsaldo.

## Offene Punkte

Keine.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-08 | Cedric Caviezel | Erste Version |

## Sonstiges

Tests: `BankTests.testBalance`, `BankTests.testAssertAccounts`, `BankTests.testIsolationFirst`,
`BankTests.testIsolationSecond`.

## Anmerkungen

Dank der abstrakten Klasse `Account` braucht die Bank kein `instanceof`: jedes Konto beantwortet
`getBalance()` selbst.
