# Klassendiagramm

Stand nach Sprint 5. Alle Klassen aus `src/main/java/ch/bbw` mit Attributen, Methoden und
Beziehungen. Beträge sind immer `long` in Millirappen (`100_000` = CHF 1.00).

```mermaid
classDiagram
    direction TB

    class Bank {
        -TreeMap accounts
        -AccountFactory accountFactory
        ~Bank(AccountFactory accountFactory)
        +getInstance() Bank$
        +reset() void$
        +createSavingsAccount() String
        +createPromoYouthSavingsAccount() String
        +createSalaryAccount(long creditLimit) String
        +getBalance() long
        +getBalance(String id) long
        +deposit(String id, Scheduled date, long amount, String text) void
        +deposit(String id, Scheduled date, long amount) void
        +withdraw(String id, Scheduled date, long amount, String text) long
        +withdraw(String id, Scheduled date, long amount) long
        +top5HighestBalances() List~Account~
        +top5LowestBalances() List~Account~
        +getAccount(String id) Account
    }

    class Holder {
        <<static nested class of Bank>>
        ~Bank INSTANCE$
    }

    class AccountFactory {
        -AtomicLong nextAccountId
        +AccountFactory()
        +AccountFactory(long startId)
        +create(AccountType type, long creditLimit) Account
        +createSavingsAccount() Account
        +createPromoYouthSavingsAccount() Account
        +createSalaryAccount(long creditLimit) Account
    }

    class AccountType {
        <<enumeration>>
        SAVINGS
        PROMO_YOUTH
        SALARY
        -String prefix
        -AccountType(String prefix)
        +getPrefix() String
    }

    class Account {
        <<abstract>>
        -String id
        -List~Booking~ bookings
        +Account(String id)
        +getId() String
        +getBalance() long
        +isInvalidTransaction(Scheduled date) boolean
        +deposit(Scheduled date, long amount, String text) long
        +deposit(Scheduled date, long amount) long
        +withdraw(Scheduled date, long amount, String text) long
        +withdraw(Scheduled date, long amount) long
        +getBookings() List~Booking~
        +printStatement() String
        +getAccountType() String*
    }

    class SavingsAccount {
        +SavingsAccount(String id)
        +withdraw(Scheduled date, long amount, String text) long
        +getAccountType() String
    }

    class PromoYouthSavingsAccount {
        -long monthlyLimit
        +PromoYouthSavingsAccount(String id)
        +withdraw(Scheduled date, long amount, String text) long
        +getAccountType() String
    }

    class SalaryAccount {
        -long creditLimit
        +SalaryAccount(String id, long creditLimit)
        +getCreditLimit() long
        +withdraw(Scheduled date, long amount, String text) long
        +getAccountType() String
    }

    class Booking {
        <<record>>
        +long date
        +long amount
        +String text
        +Booking(long date, long amount, String text)
    }

    class Scheduled {
        -long millisecondsUntilDue
        +Scheduled(long millisecondsUntilDue)
        +now() Scheduled$
        +isPastDue() boolean
        +toUnix() long
        +toInstant() Instant
        +plus(long duration, ChronoUnit unit) Scheduled
        +minus(long duration, ChronoUnit unit) Scheduled
        +plusMillis(long millisToAdd) Scheduled
        +plusSeconds(long secondsToAdd) Scheduled
        +plusMinutes(long minutesToAdd) Scheduled
        +plusHours(long hoursToAdd) Scheduled
        +plusDays(long daysToAdd) Scheduled
        +plusWeeks(long weeksToAdd) Scheduled
        +plusMonths(long monthsToAdd) Scheduled
        +plusYears(long yearsToAdd) Scheduled
        +minusMillis(long millisToAdd) Scheduled
        +minusSeconds(long secondsToAdd) Scheduled
        +minusMinutes(long minutesToAdd) Scheduled
        +minusHours(long hoursToAdd) Scheduled
        +minusDays(long daysToAdd) Scheduled
        +minusWeeks(long weeksToAdd) Scheduled
        +minusMonths(long monthsToAdd) Scheduled
        +minusYears(long yearsToAdd) Scheduled
        +toString() String
    }

    class MoneyFormatter {
        +toCHF(long millirappen) String$
    }

    class InvalidAmountException {
        +InvalidAmountException(String message)
    }

    class InvalidDateException {
        +InvalidDateException(String message)
    }

    class Exception

    class NewBankApplication {
        +main(String[] args) void$
    }

    Account <|-- SavingsAccount
    SavingsAccount <|-- PromoYouthSavingsAccount
    Account <|-- SalaryAccount
    Exception <|-- InvalidAmountException
    Exception <|-- InvalidDateException

    Bank "1" *-- "0..*" Account : accounts
    Bank "1" --> "1" AccountFactory : accountFactory
    Bank ..> Holder : getInstance()
    Holder --> Bank : INSTANCE
    Account "1" *-- "0..*" Booking : bookings

    AccountFactory ..> AccountType : uses prefix
    AccountFactory ..> SavingsAccount : creates
    AccountFactory ..> PromoYouthSavingsAccount : creates
    AccountFactory ..> SalaryAccount : creates
    Account ..> Scheduled : uses
    Account ..> MoneyFormatter : uses
    SalaryAccount ..> MoneyFormatter : uses
    Account ..> InvalidAmountException : throws
    Account ..> InvalidDateException : throws
```

## Legende

| Zeichen | Bedeutung |
|---|---|
| `+` / `-` / `~` | public / private / package-private |
| `$` | static |
| `*` nach einer Methode | abstrakt, jede Unterklasse muss sie umsetzen |
| Pfeil mit leerem Dreieck | Vererbung (`extends`) |
| Raute | Komposition: die Bank hält ihre Konten, ein Konto hält seine Buchungen |
| durchgezogener Pfeil | Assoziation: die Bank kennt ihre `AccountFactory` |
| gestrichelter Pfeil | Abhängigkeit: verwendet, erzeugt oder wirft |

## Was das Diagramm zeigt

- **Vererbung:** `Account` ist abstrakt. `SavingsAccount` verbietet das Überziehen,
  `PromoYouthSavingsAccount` ist ein Sparkonto mit zusätzlicher Monatslimite von CHF 200, und
  `SalaryAccount` darf bis zur Kreditlimite überzogen werden. Jeder Typ setzt `getAccountType()` um.
- **Factory:** Nur die `AccountFactory` erzeugt konkrete Konten. Die `Bank` kennt nur `Account`
  und importiert keine konkrete Kontoklasse.
- **Singleton:** Die einzige `Bank` liegt in der inneren Klasse `Holder` und kommt über
  `Bank.getInstance()`. Der Konstruktor ist package-private und bekommt die `AccountFactory`
  übergeben.
- **Saldo:** Es gibt kein Attribut `balance`; `getBalance()` ist die Summe aller Buchungen.
- **Konten der Bank:** `accounts` ist eine `TreeMap<String, Account>` mit der Kontonummer als
  Schlüssel (Mermaid kann Generics mit Komma nicht darstellen).
