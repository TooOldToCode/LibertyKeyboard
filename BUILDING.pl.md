# Budowanie Liberty Keyboard ze źródeł

APK można zbudować bez instalowania Android Studio. Potrzebne są Git, JDK 17,
Android SDK Command-line Tools oraz połączenie z Internetem podczas pierwszego
budowania.

Projekt wymaga Android SDK 34, a zbudowana aplikacja działa na Androidzie 10
(API 29) lub nowszym.

## Windows

Wszystkie polecenia wykonuj w PowerShellu.

### 1. Zainstaluj Git i JDK 17

```powershell
winget install --id Git.Git -e
winget install --id EclipseAdoptium.Temurin.17.JDK -e
```

Zamknij i ponownie otwórz PowerShell, a następnie sprawdź instalację:

```powershell
git --version
java -version
```

Java powinna zgłosić wersję 17.

### 2. Zainstaluj Android SDK Command-line Tools

Otwórz [oficjalną stronę Androida](https://developer.android.com/studio#command-tools),
pobierz najnowszą paczkę **Command line tools only** dla Windowsa i pozostaw ją
w folderze `Pobrane` (`Downloads`). Adres tej strony jest stały, natomiast
wersjonowana nazwa pliku ZIP zmienia się wraz z kolejnymi wydaniami.

```powershell
$Sdk = "$env:LOCALAPPDATA\Android\Sdk"
$Work = Join-Path $env:TEMP ("android-cli-" + [guid]::NewGuid())

$Archive = Get-ChildItem `
  "$HOME\Downloads\commandlinetools-win-*_latest.zip" |
  Sort-Object LastWriteTime |
  Select-Object -Last 1

if (-not $Archive) {
    throw "Nie znaleziono Command-line Tools w folderze Downloads."
}

New-Item -ItemType Directory -Force $Work | Out-Null
New-Item -ItemType Directory -Force "$Sdk\cmdline-tools" | Out-Null

Expand-Archive $Archive.FullName -DestinationPath $Work
Move-Item "$Work\cmdline-tools" "$Sdk\cmdline-tools\latest"

[Environment]::SetEnvironmentVariable("ANDROID_HOME", $Sdk, "User")
$env:ANDROID_HOME = $Sdk

$SdkManager = "$Sdk\cmdline-tools\latest\bin\sdkmanager.bat"

& $SdkManager --sdk_root="$Sdk" `
  "platform-tools" `
  "platforms;android-34" `
  "build-tools;34.0.0"

& $SdkManager --sdk_root="$Sdk" --licenses
```

Odpowiadaj `y` na pytania dotyczące licencji.

### 3. Pobierz i zbuduj projekt

```powershell
cd $HOME
git clone https://github.com/TooOldToCode/LibertyKeyboard.git
cd LibertyKeyboard
.\gradlew.bat assembleDebug
```

Gotowy APK:

```text
app\build\outputs\apk\debug\app-debug.apk
```

Możesz skopiować go na telefon i otworzyć albo zainstalować przez USB po
włączeniu debugowania USB:

```powershell
& "$Sdk\platform-tools\adb.exe" install -r `
  "app\build\outputs\apk\debug\app-debug.apk"
```

## Linux (Ubuntu/Debian)

### 1. Zainstaluj wymagane programy

```bash
sudo apt update
sudo apt install openjdk-17-jdk git unzip
java -version
```

### 2. Zainstaluj Android SDK Command-line Tools

Otwórz [oficjalną stronę Androida](https://developer.android.com/studio#command-tools),
pobierz najnowszą paczkę **Command line tools only** dla Linuxa i pozostaw ją
w katalogu `Pobrane` (`Downloads`).

```bash
export ANDROID_HOME="$HOME/Android/Sdk"
android_cli_tmp="$(mktemp -d)"
android_cli_archive="$(
  find "$HOME/Downloads" -maxdepth 1 -type f \
    -name 'commandlinetools-linux-*_latest.zip' \
    -printf '%T@ %p\n' |
  sort -nr |
  head -n 1 |
  cut -d ' ' -f 2-
)"

if [ -z "$android_cli_archive" ]; then
  echo "Nie znaleziono Command-line Tools w katalogu Downloads." >&2
  exit 1
fi

mkdir -p "$ANDROID_HOME/cmdline-tools"
unzip "$android_cli_archive" -d "$android_cli_tmp"
mv "$android_cli_tmp/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"

export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

sdkmanager --sdk_root="$ANDROID_HOME" \
  "platform-tools" \
  "platforms;android-34" \
  "build-tools;34.0.0"

sdkmanager --sdk_root="$ANDROID_HOME" --licenses
```

Odpowiadaj `y` na pytania dotyczące licencji.

### 3. Pobierz i zbuduj projekt

```bash
cd "$HOME"
git clone https://github.com/TooOldToCode/LibertyKeyboard.git
cd LibertyKeyboard
./gradlew assembleDebug
```

Gotowy APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Możesz skopiować go na telefon i otworzyć albo zainstalować przez USB po
włączeniu debugowania USB:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Kolejne aktualizacje

Po jednorazowym zainstalowaniu wymaganych narzędzi wystarczą:

Windows:

```powershell
cd "$HOME\LibertyKeyboard"
git pull
.\gradlew.bat assembleDebug
```

Linux:

```bash
cd "$HOME/LibertyKeyboard"
git pull
./gradlew assembleDebug
```

`assembleDebug` tworzy instalowalny APK podpisany automatycznym kluczem
debugowym. Jest on przeznaczony do samodzielnego budowania i testowania.
