[English](#english) | [Polski](#polski)

<a id="english"></a>

# Liberty Keyboard User Guide

## 1. Getting started

After installation, open **Liberty Keyboard** from the application list. You
will see the setup screen. It is not used for typing messages—the actual
keyboard appears in any application with a standard text field, such as a
messenger, SMS app, email client, browser, or notes app.

### “Enable keyboard”

This button opens the Android system keyboard settings.

1. Find **Liberty Keyboard** in the list.
2. Enable it and accept the system warning.
3. Open an application and tap its text field.
4. If another keyboard appears, use Android's keyboard selector or the **🌐**
   key and select **Liberty Keyboard**.

Liberty Keyboard must be enabled as an input method before it can be used.
Android displays a standard warning for every installed keyboard because a
keyboard technically handles the text entered by the user. Liberty Keyboard
processes this text locally and does not send it to the developer.

### “Enable decrypted message preview”

This button displays information about the optional Accessibility Service. The
Android settings where Liberty Keyboard can be enabled open only after selecting
**“I agree—open settings”**.

On Android 13 or newer, especially when the application was installed outside
Google Play, Liberty Keyboard may initially be greyed out on the Accessibility
screen. In that case:

1. Open Android **Settings**.
2. Select **Apps** and then **Liberty Keyboard**. If necessary, first select
   **See all apps**.
3. Open the **⋮** menu in the upper-right corner.
4. Select **Allow restricted settings** and follow the on-screen instructions.
5. Return to the Accessibility settings and enable **Liberty Keyboard**.

Menu names and their location may vary between device manufacturers and Android
versions. Allow restricted settings only if you trust the application and the
source from which it was installed.

The service is required to detect encrypted text automatically in the active
window of any application and in notifications. Its effectiveness depends on
which elements an application exposes through Android's accessibility
interface. Without this service, you can still:

- write and encrypt messages;
- manually decrypt selected text or ciphertext from the clipboard.

The Accessibility Service is therefore **optional**, but it must be enabled for
decrypted messages to appear automatically in the keyboard panel. It can be
disabled later in Android's accessibility settings.

### “Privacy Policy”

Opens the public privacy policy in a browser. The document describes local
processing of text, passwords, clipboard content, visible screen content, and
notifications.

### “User Guide”

Opens this guide in a browser.

### “About”

Displays the version number, author, information that the application was
created using vibe coding with assistance from OpenAI Codex, the license, and a
button linking to the source code.

## 2. Setting a shared password

People who want to exchange encrypted messages must set exactly the same
password. Share it through a secure channel other than the conversation it is
intended to protect.

1. Open a text field in an application and select Liberty Keyboard.
2. Tap the **key/password settings** icon in the upper-left corner.
3. Enter the password using Liberty Keyboard.
4. Tap **ACTIVATE** or the **↵** key.

The password is stored only in the memory of the running application. It may
need to be entered again after it is cleared, the process stops, or the device
restarts.

Use a long, hard-to-guess password. Installing the application does not share
the password with other participants.

## 3. Keyboard toolbar

### Key/password settings icon

Opens the session password entry panel.

In the password panel:

- **ACTIVATE**—stores the password in memory for the current session and enables
  encryption;
- **CLEAR**—removes the password and decrypted-message history.

### “ON” / “OFF”

Enables or disables automatic encryption with the **↵** key.

- **ON**—the **↵** key encrypts the text before the cursor, replaces it with
  ciphertext, and sends an Enter key event to the messenger;
- **OFF**—the keyboard enters plain text and **↵** behaves like a regular
  newline.

If no password has been set, attempting to enable **ON** opens the password
panel.

### “DECRYPT”

Starts manual decryption:

1. the application first checks text selected in the active field;
2. it then checks the current clipboard content;
3. it looks for a string beginning with `LibertyKeyboard:`;
4. it displays the decrypted result in the keyboard history panel.

This is useful when a messenger does not expose a message to the Accessibility
Service. Copy the entire ciphertext and tap **DECRYPT**.

### “CLEAR”

Removes:

- the current session password;
- decrypted messages from the history;
- the enabled encryption state.

After using this button, set the password again to encrypt or decrypt messages.

## 4. Decrypted-message panel

The panel appears above the keys after the application successfully decrypts at
least one message.

- **▲**—shows the previous message in the history;
- **▼**—shows the next message;
- **×**—removes the currently displayed message;
- long-press **×**—removes the entire history.

The history exists only in the application process memory and is not stored
permanently.

## 5. Function keys

- **↑ / ⇧**—enables uppercase for one character, then returns to lowercase;
- **⌫**—deletes a character; holding it repeats deletion;
- **?123**—switches to digits and symbols;
- **=\\<**—shows an additional set of symbols;
- **ABC**—returns to letters;
- **🌐**—switches to the next keyboard or opens the system keyboard selector;
- **space**—inserts a space; its label depends on the selected language;
- **↵**—in **ON** mode, encrypts and attempts to send the message; in **OFF**
  mode, inserts a newline.

Long-pressing some letters displays language variants and diacritical
characters. Slide your finger to the desired variant and release it.

## 6. Sending an encrypted message

1. Make sure all participants have set the same password.
2. Check that the encryption indicator shows **ON**.
3. Enter the message in the messenger's text field.
4. Tap **↵**.
5. The plain text is replaced with a string beginning with
   `LibertyKeyboard:`.
6. The keyboard sends a physical Enter key event to the messenger.

Enter behavior also depends on the messenger's settings. If it is configured so
that Enter creates a new line, the ciphertext may remain in the field or receive
a newline instead of being sent. In that case, use the messenger's send button
or change its Enter setting.

## 7. Receiving messages

With the correct password set:

- if the optional Accessibility Service is enabled, the application
  automatically looks for ciphertext in the active window and notifications;
- decrypted text appears in the keyboard history panel;
- if automatic detection does not work, copy the ciphertext and tap
  **DECRYPT**.

Ciphertext cannot be decrypted with a different password. The application does
not synchronize passwords between devices.

## 8. Troubleshooting

### The keyboard does not appear

- make sure Liberty Keyboard is enabled in the system keyboard settings;
- tap the text field again;
- use the system keyboard selector and choose Liberty Keyboard;
- if the Accessibility Service was working intensively, disable and re-enable
  it;
- restart the messenger.

### A message is not decrypted automatically

- make sure the correct password is set;
- make sure the Liberty Keyboard service is enabled in the accessibility
  settings;
- scroll the conversation to refresh visible messages;
- copy the entire `LibertyKeyboard:...` string and use **DECRYPT**.

### Enter does not send the message

Some messengers treat a physical Enter key as a newline. Check the messenger's
“Enter sends message” setting or use its on-screen send button.

## 9. Important security limitations

- Liberty Keyboard protects message content before passing it to the messenger,
  but it does not hide metadata such as participants, communication time, or
  the fact that a message was sent.
- Anyone who knows the shared password can decrypt the group's messages.
- Losing or disclosing the password compromises the confidentiality of messages
  that can be decrypted with it.
- The application does not verify the sender's identity. Anyone who knows the
  password can create valid ciphertext.
- Security depends on password strength and how it is shared.

## 10. Privacy and source code

The full [Privacy Policy](./privacy-policy.md#english) describes data processing.
The application's source code is publicly available in the
[TooOldToCode/LibertyKeyboard](https://github.com/TooOldToCode/LibertyKeyboard)
repository.

---

<a id="polski"></a>

# Instrukcja użytkownika Liberty Keyboard

## 1. Pierwsze uruchomienie

Po zainstalowaniu otwórz aplikację **Liberty Keyboard** z listy aplikacji.
Zobaczysz ekran konfiguracyjny. Nie służy on do wpisywania wiadomości — właściwa
klawiatura pojawia się w dowolnej aplikacji udostępniającej standardowe pole
tekstowe, na przykład w komunikatorze, SMS-ach, poczcie, przeglądarce albo
notatniku.

### „Włącz klawiaturę”

Ten przycisk otwiera systemowe ustawienia klawiatur Androida.

1. Znajdź **Liberty Keyboard** na liście.
2. Włącz ją i zaakceptuj systemowe ostrzeżenie.
3. Otwórz wybraną aplikację i dotknij jej pola tekstowego.
4. Jeżeli pojawi się inna klawiatura, użyj przycisku wyboru klawiatury Androida
   albo klawisza **🌐** i wybierz **Liberty Keyboard**.

Włączenie Liberty Keyboard jako metody wprowadzania jest wymagane, aby jej
używać. Android wyświetla standardowe ostrzeżenie dla każdej instalowanej
klawiatury, ponieważ klawiatura technicznie obsługuje wpisywany przez użytkownika
tekst. Liberty Keyboard przetwarza go lokalnie i nie wysyła go do dewelopera.

### „Włącz podgląd odszyfrowanych wiadomości”

Ten przycisk pokazuje informację o opcjonalnej Usłudze ułatwień dostępu. Dopiero
po wybraniu **„Zgadzam się — przejdź do ustawień”** zostaną otwarte ustawienia
Androida, w których można włączyć usługę Liberty Keyboard.

Na Androidzie 13 lub nowszym, szczególnie gdy aplikację zainstalowano spoza
Google Play, Liberty Keyboard może być początkowo wyszarzona na ekranie
ułatwień dostępu. W takim przypadku:

1. Otwórz **Ustawienia** Androida.
2. Wybierz **Aplikacje**, a następnie **Liberty Keyboard**. W razie potrzeby
   wybierz najpierw **Wyświetl wszystkie aplikacje**.
3. Otwórz menu **⋮** w prawym górnym rogu.
4. Wybierz **Zezwól na ustawienia z ograniczeniem** i wykonaj instrukcje
   wyświetlone na ekranie.
5. Wróć do ustawień ułatwień dostępu i włącz **Liberty Keyboard**.

Nazwy i położenie opcji mogą się różnić zależnie od producenta urządzenia oraz
wersji Androida. Zezwalaj na ustawienia z ograniczeniem tylko wtedy, gdy ufasz
aplikacji i źródłu, z którego została zainstalowana.

Usługa jest potrzebna do automatycznego wykrywania zaszyfrowanych tekstów
widocznych w aktywnym oknie dowolnej aplikacji i w powiadomieniach. Skuteczność
zależy od tego, jakie elementy dana aplikacja udostępnia systemowi Android przez
interfejs dostępności. Bez tej usługi nadal można:

- pisać i szyfrować wiadomości;
- ręcznie odszyfrowywać zaznaczony tekst albo szyfrogram ze schowka.

Usługa dostępności jest więc **opcjonalna**, ale trzeba ją włączyć, jeśli
odszyfrowane wiadomości mają pojawiać się automatycznie w panelu klawiatury.
Można ją później wyłączyć w ustawieniach ułatwień dostępu Androida.

### „Polityka prywatności”

Otwiera publiczną politykę prywatności w przeglądarce. Dokument opisuje lokalne
przetwarzanie tekstu, hasła, schowka, widocznej zawartości ekranu i powiadomień.

### „Instrukcja użytkownika”

Otwiera niniejszą instrukcję w przeglądarce.

### „O aplikacji”

Pokazuje numer wersji, autora, informację o tworzeniu aplikacji metodą vibe
coding przy wsparciu OpenAI Codex, licencję oraz przycisk prowadzący do kodu
źródłowego.

## 2. Ustawienie wspólnego hasła

Osoby, które chcą wymieniać zaszyfrowane wiadomości, muszą ustawić dokładnie to
samo hasło. Hasło należy przekazać bezpiecznym kanałem, innym niż rozmowa, którą
ma ono chronić.

1. Otwórz pole tekstowe w wybranej aplikacji i wybierz Liberty Keyboard.
2. Naciśnij przycisk z ikoną **klucza/ustawień hasła** w lewym górnym rogu.
3. Wpisz hasło za pomocą klawiatury Liberty Keyboard.
4. Naciśnij **AKTYWUJ** albo klawisz **↵**.

Hasło jest przechowywane wyłącznie w pamięci działającej aplikacji. Po
wyczyszczeniu hasła, zatrzymaniu procesu albo restarcie urządzenia może być
konieczne ponowne jego podanie.

Używaj długiego i trudnego do odgadnięcia hasła. Sama instalacja aplikacji nie
przekazuje hasła pozostałym rozmówcom.

## 3. Górny pasek klawiatury

### Ikona klucza/ustawień hasła

Otwiera panel wprowadzania hasła sesji.

W panelu hasła:

- **AKTYWUJ** — zapamiętuje hasło w pamięci bieżącej sesji i włącza szyfrowanie;
- **WYCZYŚĆ** — usuwa hasło oraz historię odszyfrowanych wiadomości.

### „ON” / „OFF”

Włącza albo wyłącza automatyczne szyfrowanie przy użyciu klawisza **↵**.

- **ON** — klawisz **↵** szyfruje tekst przed kursorem, zastępuje go szyfrogramem
  i wysyła zdarzenie Enter do komunikatora;
- **OFF** — klawiatura wpisuje zwykły tekst, a **↵** działa jak zwykły znak
  nowej linii.

Jeżeli hasło nie zostało jeszcze ustawione, próba włączenia trybu **ON** otworzy
panel hasła.

### „ODSZYFRUJ”

Uruchamia ręczne odszyfrowanie:

1. aplikacja najpierw sprawdza tekst zaznaczony w aktywnym polu;
2. następnie sprawdza aktualną zawartość schowka;
3. szuka ciągu rozpoczynającego się od `LibertyKeyboard:`;
4. pokazuje odszyfrowany wynik w panelu historii klawiatury.

Funkcja jest przydatna, gdy komunikator nie udostępnił wiadomości Usłudze
ułatwień dostępu. Można wtedy skopiować cały szyfrogram i nacisnąć
**ODSZYFRUJ**.

### „USUŃ”

Usuwa:

- hasło bieżącej sesji;
- odszyfrowane wiadomości z historii;
- stan włączonego szyfrowania.

Po użyciu tego przycisku trzeba ponownie ustawić hasło, aby szyfrować lub
odszyfrowywać wiadomości.

## 4. Panel odszyfrowanych wiadomości

Panel pojawia się nad klawiszami, kiedy aplikacja poprawnie odszyfruje co najmniej
jedną wiadomość.

- **▲** — pokazuje poprzednią wiadomość w historii;
- **▼** — pokazuje następną wiadomość;
- **×** — usuwa aktualnie wyświetlaną wiadomość;
- długie przytrzymanie **×** — usuwa całą historię.

Historia istnieje tylko w pamięci procesu aplikacji i nie jest zapisywana na
stałe.

## 5. Klawisze funkcyjne

- **↑ / ⇧** — jednorazowo włącza wielką literę; po wpisaniu litery wraca do
  małych liter;
- **⌫** — usuwa znak; przytrzymanie powoduje powtarzanie kasowania;
- **?123** — przechodzi do cyfr i symboli;
- **=\\<** — pokazuje dodatkowy zestaw symboli;
- **ABC** — wraca do liter;
- **🌐** — przełącza na następną klawiaturę lub otwiera systemowy wybór
  klawiatury;
- **spacja** — wstawia spację; jej opis zależy od wybranego języka;
- **↵** — w trybie **ON** szyfruje i próbuje wysłać wiadomość, a w trybie
  **OFF** wstawia nową linię.

Dłuższe przytrzymanie niektórych liter wyświetla warianty językowe i znaki
diakrytyczne. Przesuń palec nad wybrany wariant i puść.

## 6. Wysłanie zaszyfrowanej wiadomości

1. Upewnij się, że rozmówcy mają ustawione to samo hasło.
2. Sprawdź, czy wskaźnik szyfrowania pokazuje **ON**.
3. Wpisz wiadomość w polu komunikatora.
4. Naciśnij **↵**.
5. Zwykły tekst zostanie zastąpiony ciągiem zaczynającym się od
   `LibertyKeyboard:`.
6. Klawiatura wyśle do komunikatora zdarzenie fizycznego klawisza Enter.

Zachowanie Entera zależy również od ustawień komunikatora. Jeśli komunikator ma
ustawione „Enter tworzy nową linię”, szyfrogram może pozostać w polu lub otrzymać
nową linię zamiast zostać wysłany. W takim przypadku użyj przycisku wysyłania
komunikatora albo zmień jego ustawienie Entera.

## 7. Odbieranie wiadomości

Przy ustawionym prawidłowym haśle:

- jeżeli opcjonalna usługa dostępności jest włączona, aplikacja automatycznie
  szuka szyfrogramów w aktywnym oknie i powiadomieniach;
- odszyfrowany tekst pojawia się w panelu historii klawiatury;
- jeżeli automatyczne wykrycie nie zadziała, skopiuj szyfrogram i naciśnij
  **ODSZYFRUJ**.

Szyfrogramu nie można odszyfrować innym hasłem. Aplikacja nie synchronizuje haseł
między urządzeniami.

## 8. Rozwiązywanie problemów

### Klawiatura nie pojawia się

- sprawdź, czy Liberty Keyboard jest włączona w systemowych ustawieniach
  klawiatur;
- dotknij ponownie pola tekstowego;
- użyj systemowego wyboru klawiatury i wybierz Liberty Keyboard;
- jeśli usługa dostępności intensywnie pracowała, wyłącz ją i włącz ponownie;
- uruchom ponownie komunikator.

### Wiadomość nie odszyfrowuje się automatycznie

- sprawdź, czy ustawiono właściwe hasło;
- sprawdź, czy usługa Liberty Keyboard jest włączona w ustawieniach dostępności;
- przewiń rozmowę, aby odświeżyć widoczne wiadomości;
- skopiuj cały ciąg `LibertyKeyboard:...` i użyj przycisku **ODSZYFRUJ**.

### Enter nie wysyła wiadomości

Niektóre komunikatory traktują fizyczny Enter jako nową linię. Sprawdź ustawienie
„Enter wysyła wiadomość” w komunikatorze albo użyj jego ekranowego przycisku
wysyłania.

## 9. Ważne ograniczenia bezpieczeństwa

- Liberty Keyboard chroni treść przed wysłaniem jej do komunikatora, ale nie
  ukrywa metadanych, takich jak rozmówcy, czas komunikacji czy fakt wysłania
  wiadomości.
- Każda osoba znająca wspólne hasło może odszyfrować wiadomości tej grupy.
- Utrata lub ujawnienie hasła oznacza utratę poufności wiadomości, które można nim
  odszyfrować.
- Aplikacja nie weryfikuje tożsamości nadawcy. Osoba znająca hasło może utworzyć
  poprawny szyfrogram.
- Bezpieczeństwo zależy od siły hasła i sposobu jego przekazania.

## 10. Prywatność i kod źródłowy

Pełna [polityka prywatności](./privacy-policy.md#polski) opisuje przetwarzanie danych.
Kod źródłowy aplikacji jest publicznie dostępny w repozytorium
[TooOldToCode/LibertyKeyboard](https://github.com/TooOldToCode/LibertyKeyboard).
