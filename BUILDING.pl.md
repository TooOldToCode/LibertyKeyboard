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

```powershell
$Sdk = "$env:LOCALAPPDATA\Android\Sdk"
$Work = Join-Path $env:TEMP ("android-cli-" + [guid]::NewGuid())
$Archive = Join-Path $Work "command-line-tools.zip"

New-Item -ItemType Directory -Force $Work | Out-Null
New-Item -ItemType Directory -Force "$Sdk\cmdline-tools" | Out-Null

Invoke-WebRequest `
  "https://dl.google.com/android/repository/commandlinetools-win-15859902_latest.zip" `
  -OutFile $Archive

Expand-Archive $Archive -DestinationPath $Work
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

Aktualna wersja Command-line Tools jest dostępna na
[oficjalnej stronie Androida](https://developer.android.com/studio#command-tools).
Jeżeli podany wyżej adres paczki przestanie działać, pobierz stamtąd najnowszą
wersję dla Windowsa i rozpakuj jej zawartość do
`%LOCALAPPDATA%\Android\Sdk\cmdline-tools\latest`.

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
sudo apt install openjdk-17-jdk git unzip wget
java -version
```

### 2. Zainstaluj Android SDK Command-line Tools

```bash
export ANDROID_HOME="$HOME/Android/Sdk"
android_cli_tmp="$(mktemp -d)"

mkdir -p "$ANDROID_HOME/cmdline-tools"
wget -O "$android_cli_tmp/command-line-tools.zip" \
  "https://dl.google.com/android/repository/commandlinetools-linux-15859902_latest.zip"
unzip "$android_cli_tmp/command-line-tools.zip" -d "$android_cli_tmp"
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
