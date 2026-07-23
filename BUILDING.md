# Building Liberty Keyboard from source

You can build the APK without installing Android Studio. You need Git, JDK 17,
the Android SDK Command-line Tools, and an Internet connection during the first
build.

The project requires Android SDK 34. The resulting application runs on Android
10 (API 29) or newer.

## Windows

Run all commands in PowerShell.

### 1. Install Git and JDK 17

```powershell
winget install --id Git.Git -e
winget install --id EclipseAdoptium.Temurin.17.JDK -e
```

Close and reopen PowerShell, then verify the installation:

```powershell
git --version
java -version
```

Java should report version 17.

### 2. Install the Android SDK Command-line Tools

Open the [official Android website](https://developer.android.com/studio#command-tools),
download the latest **Command line tools only** package for Windows, and leave
it in your `Downloads` folder. This page has a stable address, while the
versioned ZIP filename changes with each release.

```powershell
$Sdk = "$env:LOCALAPPDATA\Android\Sdk"
$Work = Join-Path $env:TEMP ("android-cli-" + [guid]::NewGuid())

$Archive = Get-ChildItem `
  "$HOME\Downloads\commandlinetools-win-*_latest.zip" |
  Sort-Object LastWriteTime |
  Select-Object -Last 1

if (-not $Archive) {
    throw "Command-line Tools were not found in the Downloads folder."
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

Answer `y` when prompted to accept the licences.

### 3. Download and build the project

```powershell
cd $HOME
git clone https://github.com/TooOldToCode/LibertyKeyboard.git
cd LibertyKeyboard
.\gradlew.bat assembleDebug
```

The generated APK is located at:

```text
app\build\outputs\apk\debug\app-debug.apk
```

Copy it to your phone and open it, or install it over USB after enabling USB
debugging:

```powershell
& "$Sdk\platform-tools\adb.exe" install -r `
  "app\build\outputs\apk\debug\app-debug.apk"
```

## Linux (Ubuntu/Debian)

### 1. Install the required software

```bash
sudo apt update
sudo apt install openjdk-17-jdk git unzip
java -version
```

### 2. Install the Android SDK Command-line Tools

Open the [official Android website](https://developer.android.com/studio#command-tools),
download the latest **Command line tools only** package for Linux, and leave it
in your `Downloads` directory.

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
  echo "Command-line Tools were not found in the Downloads directory." >&2
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

Answer `y` when prompted to accept the licences.

### 3. Download and build the project

```bash
cd "$HOME"
git clone https://github.com/TooOldToCode/LibertyKeyboard.git
cd LibertyKeyboard
./gradlew assembleDebug
```

The generated APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Copy it to your phone and open it, or install it over USB after enabling USB
debugging:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Subsequent updates

After the one-time tool installation, you only need:

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

`assembleDebug` creates an installable APK signed automatically with a debug
key. It is suitable for personal builds and testing.
