# Google Play — deklaracja AccessibilityService

Poniższy tekst można wkleić do formularza po angielsku. Nazwy i kolejność pól
mogą się nieznacznie różnić w Play Console.

## Classification

**Is your app an accessibility tool?**

`No`

Liberty Keyboard is a privacy and encryption utility. Its primary purpose is not
to assist users with disabilities, therefore the app does not declare
`isAccessibilityTool=true`.

## Why the API is required

**Selected purpose:** `App functionality`

**Suggested description:**

> Liberty Keyboard provides an optional feature that detects Liberty Keyboard
> ciphertext displayed in messaging applications and shows the locally decrypted
> text in the keyboard. Android messaging applications do not expose their
> rendered message content through a narrower general-purpose API. When the user
> explicitly enables the feature, AccessibilityService reads text and content
> descriptions from the active window and reads notification text made available
> by Android. It searches only for strings beginning with "LibertyKeyboard:" and
> attempts local decryption using the password held in app memory. Raw screen and
> notification text is not persisted or transmitted off the device. The service
> does not perform gestures, click controls, enter text, send messages, change
> settings, or make decisions on the user's behalf. The feature is optional and
> the keyboard can operate without it.

## Data accessed

> The service can access text and content descriptions exposed in the active
> accessibility window, notification text, and window/content-change events. It
> uses this access only to identify Liberty Keyboard ciphertext and decrypt it
> locally. Decrypted results and a limited deduplication record are held only in
> process memory. No accessed data is sent to the developer or shared with third
> parties.

## Prominent disclosure and consent

**Disclosure shown in the app before opening Android Accessibility settings:**

> To automatically detect encrypted messages, Liberty Keyboard uses an
> Accessibility Service. If enabled, the app can read text and element
> descriptions visible in the active window and notification content from other
> apps.
>
> This data is used only to find messages beginning with "LibertyKeyboard:" and
> decrypt them locally. It is not transmitted off the device or shared with third
> parties. Raw screen and notification content is not saved. Decrypted messages
> are temporarily retained only in app memory until the user deletes them, clears
> the password, or the app process ends.
>
> The service does not click controls, send messages, or modify other apps. The
> feature is optional and can be disabled in Android Accessibility settings.

Buttons:

- `I do not agree` — closes the dialog and does not open settings;
- `I agree — open settings` — affirmative consent and only then opens Android
  Accessibility settings.

The shipping app currently presents the equivalent disclosure in Polish.

## Review video checklist

Przygotuj krótki film z widocznym ekranem urządzenia:

1. Uruchom Liberty Keyboard od ikony aplikacji.
2. Naciśnij „Włącz podgląd odszyfrowanych wiadomości”.
3. Pokaż cały dialog ujawnienia — tekst musi być czytelny.
4. Pokaż ścieżkę odmowy: „Nie zgadzam się”; ustawienia nie otwierają się.
5. Ponownie otwórz dialog i wybierz „Zgadzam się — przejdź do ustawień”.
6. W ustawieniach Androida włącz usługę Liberty Keyboard.
7. Otwórz komunikator i pokaż nadejście szyfrogramu `LibertyKeyboard:...`.
8. Pokaż odszyfrowany wynik w panelu klawiatury.
9. Dobrze dodać angielskie napisy wyjaśniające lokalne przetwarzanie.

Film powinien być niepubliczny (`unlisted`) w YouTube albo dostępny recenzentom
przez Google Drive. Link wklej do formularza deklaracji.

## Claims to avoid

- Nie deklaruj `isAccessibilityTool=true`.
- Nie opisuj usługi jako automatyzacji.
- Nie twierdź, że usługa odczytuje wyłącznie zaszyfrowany tekst — technicznie ma
  dostęp do całego tekstu wystawionego przez aktywne okno i powiadomienia, choć
  wykorzystuje go tylko do wyszukania prefiksu.
- Nie deklaruj, że odszyfrowana historia nie jest przechowywana; jest tymczasowo
  przechowywana w pamięci procesu.
- Po zmianie sposobu używania AccessibilityService zaktualizuj deklarację i film.
