# Build-Anleitung / Build Instructions

## Voraussetzungen / Prerequisites

- **Android Studio**: Flamingo (2022.2.1) oder neuer / or newer
- **JDK**: Version 17 oder höher / or higher
- **Android SDK**: API Level 34 (Android 14)
- **Minimum SDK**: API Level 24 (Android 7.0)

## Installation

### 1. Repository klonen / Clone Repository

```bash
git clone <repository-url>
cd Song-To-Drumsheet
```

### 2. Android Studio öffnen / Open Android Studio

1. Öffnen Sie Android Studio / Open Android Studio
2. Wählen Sie "Open an Existing Project" / Select "Open an Existing Project"
3. Navigieren Sie zum Projektordner / Navigate to project folder
4. Warten Sie, bis Gradle die Abhängigkeiten synchronisiert / Wait for Gradle to sync dependencies

### 3. SDK-Pfad konfigurieren / Configure SDK Path

Erstellen Sie eine `local.properties` Datei im Hauptverzeichnis / Create a `local.properties` file in the root directory:

```properties
sdk.dir=/path/to/your/android/sdk
```

**Beispiele / Examples:**
- Windows: `sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk`
- macOS: `sdk.dir=/Users/YourName/Library/Android/sdk`
- Linux: `sdk.dir=/home/YourName/Android/Sdk`

### 4. Gradle Sync

Android Studio sollte automatisch einen Gradle Sync durchführen. Falls nicht / Android Studio should automatically perform a Gradle sync. If not:

1. Klicken Sie auf "File" → "Sync Project with Gradle Files"
2. Warten Sie, bis alle Abhängigkeiten heruntergeladen sind / Wait for all dependencies to download

## Build-Prozess / Build Process

### Debug Build

```bash
./gradlew assembleDebug
```

Die APK wird erstellt in / The APK will be created at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release Build

```bash
./gradlew assembleRelease
```

Die APK wird erstellt in / The APK will be created at:
```
app/build/outputs/apk/release/app-release.apk
```

**Hinweis / Note**: Für einen Release Build benötigen Sie einen Signing Key / For a release build, you need a signing key.

### App auf Gerät installieren / Install App on Device

#### Über Android Studio / Via Android Studio

1. Verbinden Sie Ihr Android-Gerät oder starten Sie einen Emulator / Connect your Android device or start an emulator
2. Klicken Sie auf den "Run" Button (grünes Play-Symbol) / Click the "Run" button (green play icon)
3. Wählen Sie Ihr Gerät aus der Liste / Select your device from the list

#### Über Kommandozeile / Via Command Line

```bash
# Debug installieren / Install debug
./gradlew installDebug

# Release installieren / Install release
./gradlew installRelease
```

## Entwicklung / Development

### Projekt-Struktur / Project Structure

```
Song-To-Drumsheet/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/songtodrumsheet/app/
│   │       │   ├── MainActivity.kt                 # Hauptaktivität / Main Activity
│   │       │   ├── audio/
│   │       │   │   └── BeatDetector.kt             # Audio-Analyse / Audio Analysis
│   │       │   ├── generator/
│   │       │   │   └── DrumNotationGenerator.kt    # Noten-Generator / Note Generator
│   │       │   ├── model/
│   │       │   │   └── DrumNotation.kt             # Datenmodelle / Data Models
│   │       │   ├── export/
│   │       │   │   └── ExportManager.kt            # PDF/PNG Export
│   │       │   └── ui/
│   │       │       ├── DrumSheetView.kt            # Noten-Ansicht / Sheet View
│   │       │       └── theme/                      # App-Theme
│   │       ├── res/                                # Ressourcen / Resources
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### Wichtige Bibliotheken / Important Libraries

- **TarsosDSP**: Audio-Verarbeitung und Beat-Erkennung / Audio processing and beat detection
- **Jetpack Compose**: Moderne Android UI / Modern Android UI
- **PDFBox-Android**: PDF-Generierung / PDF generation
- **Kotlin Coroutines**: Asynchrone Operationen / Asynchronous operations

## Fehlerbehebung / Troubleshooting

### Gradle Sync Fehler / Gradle Sync Errors

```bash
# Gradle Cache löschen / Clear Gradle cache
./gradlew clean

# Gradle Wrapper neu laden / Reload Gradle wrapper
./gradlew wrapper --gradle-version=8.2
```

### Build Fehler / Build Errors

1. Überprüfen Sie die SDK-Installation / Check SDK installation
2. Stellen Sie sicher, dass API Level 34 installiert ist / Ensure API Level 34 is installed
3. Löschen Sie den Build-Ordner / Delete the build folder:
   ```bash
   rm -rf .gradle build app/build
   ```

### Laufzeitfehler / Runtime Errors

- **Berechtigungen**: Stellen Sie sicher, dass die App Zugriff auf Speicher hat / Make sure the app has storage access
- **Audio-Dateien**: Unterstützte Formate: MP3, WAV, OGG / Supported formats: MP3, WAV, OGG

## Tests ausführen / Running Tests

```bash
# Unit Tests
./gradlew test

# Instrumentierte Tests (benötigt verbundenes Gerät)
# Instrumented tests (requires connected device)
./gradlew connectedAndroidTest
```

## Code-Qualität / Code Quality

```bash
# Lint-Prüfung / Lint check
./gradlew lint

# Lint-Report anzeigen / View lint report
open app/build/reports/lint-results.html
```

## Weitere Informationen / More Information

- [Android Developer Documentation](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [TarsosDSP](https://github.com/JorenSix/TarsosDSP)
