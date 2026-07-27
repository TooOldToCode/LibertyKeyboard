# Google Play — Data safety

Stan zgodny z kodem aplikacji z 27 lipca 2026 r. Odpowiedzi należy ponownie
zweryfikować po dodaniu SDK, analityki, raportowania awarii, sieci lub nowych
funkcji.

## Data collection and security

**Does your app collect or share any of the required user data types?**

`No`

Uzasadnienie: aplikacja przetwarza tekst, hasło, zawartość aktywnego okna,
powiadomienia i schowek wyłącznie na urządzeniu. Nie ma uprawnienia `INTERNET`,
backendu, reklam, analityki ani telemetrii. Według definicji formularza Google
Play dostęp wyłącznie na urządzeniu, bez transmisji poza urządzenie, nie jest
„collection”.

**Is all of the user data collected by your app encrypted in transit?**

To pytanie nie powinno być wyświetlone po odpowiedzi `No`. Jeżeli konsola mimo
to wymaga odpowiedzi, wybierz odpowiedź wskazującą, że aplikacja nie zbiera ani
nie przesyła danych. Nie zaznaczaj twierdzenia o transmisji HTTPS — aplikacja nie
wykonuje transmisji.

**Do you provide a way for users to request that their data is deleted?**

`No` / `Not applicable`, zależnie od wariantu formularza.

Uzasadnienie: aplikacja nie tworzy kont i deweloper nie zbiera ani nie
przechowuje danych użytkowników na serwerze. Dane tymczasowe można usunąć lokalnie
przez wyczyszczenie historii i hasła, zakończenie procesu lub odinstalowanie
aplikacji.

**Independent security review**

`No`, dopóki aplikacja rzeczywiście nie przejdzie kwalifikowanego niezależnego
audytu. Publiczny kod źródłowy nie jest tym samym co niezależny audyt.

**UPI payments verification**

`No` / `Not applicable`.

## Data types

Nie zaznaczaj żadnych typów danych jako collected lub shared, o ile aktualny stan
aplikacji się nie zmieni. Dotyczy to także:

- Messages / Other in-app messages — tekst jest przetwarzany lokalnie;
- App activity — brak telemetrii;
- Files and docs / Clipboard — lokalne przetwarzanie na żądanie;
- Device or other identifiers — aplikacja ich nie pobiera;
- Crash logs and diagnostics — brak automatycznego raportowania;
- Personal info — brak kont i formularzy.

## Ważne rozróżnienie

Odpowiedź `No` w Data safety nie zwalnia z opisania lokalnego dostępu do danych w
polityce prywatności ani z prominent disclosure i zgody dla AccessibilityService.
Data safety pyta głównie o transmisję danych poza urządzenie; deklaracja
Accessibility API pyta również o sam dostęp i lokalne użycie.
