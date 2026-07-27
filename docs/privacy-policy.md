# Polityka prywatności Liberty Keyboard

**Data wejścia w życie: 27 lipca 2026 r.**

**Ostatnia aktualizacja: 27 lipca 2026 r.**

Niniejsza polityka dotyczy aplikacji Android **Liberty Keyboard**
(`pl.pf.libertykeyboard`), udostępnianej przez dewelopera **TooOldToCode**.

## Najważniejsza informacja

Liberty Keyboard nie wysyła danych użytkownika, wpisywanych tekstów, haseł,
zaszyfrowanych ani odszyfrowanych wiadomości do dewelopera lub innych podmiotów.
Aplikacja nie zawiera reklam, analityki, modułów śledzących ani własnej
komunikacji sieciowej. Przetwarzanie opisane poniżej odbywa się lokalnie na
urządzeniu użytkownika.

## Dane przetwarzane przez klawiaturę

Jako systemowa metoda wprowadzania tekstu Liberty Keyboard otrzymuje znaki
wprowadzane przez użytkownika za pomocą jej klawiszy. Są one przekazywane do
aktualnie wybranego pola tekstowego innej aplikacji. Jeżeli użytkownik włączy
szyfrowanie, tekst przed kursorem jest lokalnie szyfrowany i zastępowany
szyfrogramem rozpoczynającym się od `LibertyKeyboard:`.

Hasło sesji:

- jest podawane wewnątrz interfejsu klawiatury;
- jest przechowywane wyłącznie w pamięci operacyjnej procesu aplikacji;
- nie jest zapisywane w plikach, preferencjach ani kopii zapasowej;
- jest usuwane po użyciu funkcji „Usuń hasło” albo zakończeniu procesu aplikacji;
- nie jest przesyłane poza urządzenie.

## Opcjonalna Usługa ułatwień dostępu

Automatyczne wykrywanie i podgląd odszyfrowanych wiadomości jest funkcją
opcjonalną. Po wyrażeniu zgody i samodzielnym włączeniu usługi w ustawieniach
Androida Liberty Keyboard może uzyskać dostęp do:

- tekstu i opisów elementów widocznych w aktywnym oknie innych aplikacji;
- treści powiadomień udostępnionych przez system Android;
- zmian okien i ich zawartości niezbędnych do wykrycia nowej wiadomości.

Dostęp ten jest używany wyłącznie do wyszukania ciągów rozpoczynających się od
`LibertyKeyboard:` i próby ich lokalnego odszyfrowania przy użyciu hasła sesji.
Usługa nie wykonuje kliknięć, nie wprowadza tekstu, nie wysyła wiadomości i nie
zmienia zawartości innych aplikacji.

Surowa treść ekranu i powiadomień nie jest zapisywana. Aplikacja tymczasowo
przechowuje w pamięci:

- ograniczoną techniczną listę rozpoznanych szyfrogramów, aby nie wyświetlać
  wielokrotnie tej samej wiadomości;
- historię odszyfrowanych wiadomości widoczną w panelu klawiatury.

Historia może zostać usunięta przez użytkownika z panelu klawiatury. Jest również
usuwana po wyczyszczeniu hasła sesji lub zakończeniu procesu aplikacji.

Usługę można w każdej chwili wyłączyć w ustawieniach ułatwień dostępu Androida.
Klawiatura i ręczne szyfrowanie mogą działać bez tej usługi.

## Schowek

Funkcja ręcznego odszyfrowania, uruchamiana świadomie przez użytkownika, sprawdza
aktualnie zaznaczony tekst, a następnie zawartość schowka, w poszukiwaniu
szyfrogramu Liberty Keyboard. Zawartość schowka jest przetwarzana lokalnie i nie
jest przesyłana ani trwale zapisywana.

## Zbieranie, udostępnianie i sprzedaż danych

Liberty Keyboard:

- nie przesyła danych użytkownika poza urządzenie;
- nie zbiera danych na serwerach dewelopera;
- nie udostępnia danych osobom trzecim;
- nie sprzedaje danych;
- nie tworzy kont użytkowników;
- nie używa reklam, telemetrii ani analityki.

Wiadomość przesłana przez użytkownika za pomocą wybranego komunikatora podlega
również zasadom prywatności tego komunikatora. Liberty Keyboard nie kontroluje
sposobu, w jaki aplikacja docelowa przetwarza wysłany szyfrogram.

## Bezpieczeństwo

Wiadomości są szyfrowane lokalnie za pomocą AES-256-GCM. Klucz dla każdej
wiadomości jest wyprowadzany z hasła przy użyciu Argon2id i losowej soli.
Bezpieczeństwo zależy między innymi od siły hasła oraz bezpiecznego przekazania go
rozmówcom. Publikacja kodu źródłowego nie stanowi gwarancji całkowitego
bezpieczeństwa ani anonimowości.

## Przechowywanie i usuwanie danych

Aplikacja nie prowadzi zdalnej bazy danych i nie ma kont, dlatego deweloper nie
posiada danych użytkownika, które mógłby usunąć na żądanie. Dane tymczasowe można
usunąć za pomocą funkcji czyszczenia historii i hasła, przez zakończenie procesu
aplikacji albo odinstalowanie aplikacji.

## Dzieci

Aplikacja nie jest kierowana specjalnie do dzieci i deweloper świadomie nie
zbiera danych dzieci ani innych użytkowników.

## Zmiany polityki

Zmiany polityki będą publikowane na tej stronie wraz z nową datą aktualizacji.
Jeżeli zmieni się sposób dostępu do danych lub ich wykorzystywania, odpowiednie
deklaracje i ujawnienia w aplikacji zostaną również zaktualizowane.

## Kontakt

Deweloper: **TooOldToCode**

Kod źródłowy: <https://github.com/TooOldToCode/LibertyKeyboard>

Pytania dotyczące prywatności:
<https://github.com/TooOldToCode/LibertyKeyboard/issues>

---

# Liberty Keyboard Privacy Policy

**Effective date: July 27, 2026**

**Last updated: July 27, 2026**

This policy applies to the **Liberty Keyboard** Android application
(`pl.pf.libertykeyboard`) provided by the developer **TooOldToCode**.

## Summary

Liberty Keyboard does not transmit user data, typed text, passwords, encrypted
messages, or decrypted messages to the developer or any third party. The app
contains no advertising, analytics, tracking modules, or proprietary network
communication. The processing described below takes place locally on the user's
device.

## Data processed by the keyboard

As a system input method, Liberty Keyboard receives characters entered by the
user through its keys and passes them to the currently selected text field. When
encryption is enabled, text before the cursor is encrypted locally and replaced
with ciphertext beginning with `LibertyKeyboard:`.

The session password:

- is entered inside the keyboard interface;
- exists only in the application process memory;
- is not saved to files, preferences, or backups;
- is removed when the user clears the password or the app process ends;
- is never transmitted off the device.

## Optional Accessibility Service

Automatic detection and preview of decrypted messages is optional. After the
user gives consent and manually enables the service in Android settings, Liberty
Keyboard may access:

- text and element descriptions visible in the active window of other apps;
- notification content exposed by Android;
- window and content changes required to detect a new message.

This access is used only to locate strings beginning with `LibertyKeyboard:` and
attempt to decrypt them locally with the session password. The service does not
click controls, enter text, send messages, or alter other applications.

Raw screen and notification content is not saved. The app temporarily retains
in memory:

- a limited technical record of recognized ciphertexts to prevent duplicate
  display;
- decrypted-message history shown in the keyboard panel.

The user can delete this history from the keyboard. It is also removed when the
session password is cleared or the application process ends.

The service can be disabled at any time in Android Accessibility settings.
Keyboard input and manual encryption can operate without it.

## Clipboard

When the user explicitly invokes manual decryption, the app checks selected text
and then clipboard content for Liberty Keyboard ciphertext. Clipboard content is
processed locally and is neither transmitted nor persistently stored.

## Collection, sharing, and sale

Liberty Keyboard:

- does not transmit user data off the device;
- does not collect data on developer servers;
- does not share data with third parties;
- does not sell data;
- does not create user accounts;
- does not use advertising, telemetry, or analytics.

Messages sent through another messaging application are also subject to that
application's privacy practices. Liberty Keyboard does not control how the
destination application processes the ciphertext.

## Security

Messages are encrypted locally using AES-256-GCM. A per-message key is derived
from the password using Argon2id and a random salt. Security depends in part on
password strength and on sharing the password securely. Source availability is
not a guarantee of complete security or anonymity.

## Retention and deletion

The app has no remote database or user accounts, so the developer holds no user
data that can be deleted upon request. Temporary data can be removed by clearing
the password and history, terminating the app process, or uninstalling the app.

## Children

The app is not specifically directed to children, and the developer does not
knowingly collect data from children or other users.

## Changes

Changes will be posted on this page with an updated revision date. If data access
or use changes, the app disclosures and relevant declarations will also be
updated.

## Contact

Developer: **TooOldToCode**

Source code: <https://github.com/TooOldToCode/LibertyKeyboard>

Privacy inquiries: <https://github.com/TooOldToCode/LibertyKeyboard/issues>
