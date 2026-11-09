# UC-01 Konto eröffnen

| Feld | Inhalt |
|---|---|
| **Name** | Konto eröffnen |
| **Szenario** | Bank 2 – Kunde am Bankschalter ([`01_UML_Bank2.pdf`](../scenarios/01_UML_Bank2.pdf)), Kontotypen ([`04_UML_Bank4c.pdf`](../scenarios/04_UML_Bank4c.pdf), [`06_UML_Bank6.pdf`](../scenarios/06_UML_Bank6.pdf)) |
| **Sprint** | 1 – Baseline, 3 – Kontotypen, 4 – Factory |

## Kurzbeschreibung

Eine Kundin oder ein Kunde eröffnet am Bankschalter ein neues Konto. Es gibt drei Kontotypen:
Sparkonto, Promo-Jugendsparkonto und Lohnkonto (mit Kreditlimite). Die Bank lässt das Konto von der
`AccountFactory` erzeugen, speichert es und gibt die neue Kontonummer zurück. Das neue Konto hat den
Saldo 0 und keine Buchungen.

## Akteure

- **Kunde / Kundin** (primärer Akteur): will ein Konto haben.
- **Bank** (System): verwaltet die Konten.
- **AccountFactory** (Teil des Systems): wählt den Kontotyp und vergibt die Kontonummer.

## Auslöser

Die Kundin oder der Kunde verlangt am Bankschalter ein neues Konto eines bestimmten Typs.

## Ergebnisse

- Ein neues Konto des gewünschten Typs ist in der Bank registriert.
- Die Kundin oder der Kunde erhält die Kontonummer.

## Vorbedingung

Die Bank existiert und ist betriebsbereit (`Bank.getInstance()`).

## Nachbedingung

- **Erfolg:** Die Bank enthält ein Konto mehr als vorher. Die Kontonummer ist eindeutig, der Saldo
  ist 0. Ein Lohnkonto kennt seine Kreditlimite.
- **Fehler:** Es wurde kein Konto angelegt.

## Eingehende Daten

| Daten | Typ | Regel |
|---|---|---|
| Kontotyp | Methode der Bank | `createSavingsAccount()`, `createPromoYouthSavingsAccount()` oder `createSalaryAccount(long)` |
| Kreditlimite | `long` (Millirappen) | nur beim Lohnkonto; muss 0 oder negativ sein |

Die Kontonummer wird nicht vom Kunden angegeben, sondern von der `AccountFactory` erzeugt: Präfix
des Typs (`S-` Sparkonto, `Y-` Promo-Jugendsparkonto, `P-` Lohnkonto) und ein Zähler ab 1000, den
alle Typen teilen (`S-1000`, `Y-1001`, `P-1002`).

## Essenzieller Ablauf

1. Der Kunde verlangt ein neues Konto eines Typs.
2. Die Bank ruft die `AccountFactory` auf.
3. Die Factory holt die nächste Nummer (thread-safe) und setzt das Präfix des Typs davor.
4. Die Factory erzeugt das Konto des richtigen Typs.
5. Die Bank speichert das Konto unter seiner Kontonummer.
6. Die Bank gibt die Kontonummer zurück.

### Alternativabläufe

- **4a. Lohnkonto mit positiver Kreditlimite:** Das Konto wird mit `IllegalArgumentException`
  abgelehnt.

## Offene Punkte

- Die Kontonummer ist nur so lange eindeutig, wie die Anwendung läuft; nach einem Neustart
  beginnt der Zähler wieder bei 1000.

## Änderungshistorie

| Datum | Autor | Änderung |
|---|---|---|
| 2026-11-02 | Cedric Caviezel | Erste Version (Sprint 1) |
| 2026-11-08 | Cedric Caviezel | Drei Kontotypen, AccountFactory, thread-sichere Nummern (Sprint 3–5) |

## Sonstiges

Tests: `BankTests.testCreate`, `AccountTests.testInit`, `AccountFactoryTests.*`,
`AccountTypeTests.*`, `SalaryAccountTests.testRejectPositiveCreditLimit`.

## Anmerkungen

Beträge sind in Millirappen (`100_000` = CHF 1.00).
