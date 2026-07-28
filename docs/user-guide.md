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

Pełna [polityka prywatności](privacy-policy.html) opisuje przetwarzanie danych.
Kod źródłowy aplikacji jest publicznie dostępny w repozytorium
[TooOldToCode/LibertyKeyboard](https://github.com/TooOldToCode/LibertyKeyboard).
