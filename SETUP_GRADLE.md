# Gradle Wrapper Setup

Das Gradle Wrapper JAR fehlt im Repository (um die Repository-Größe klein zu halten).

## Für Entwickler: Gradle Wrapper generieren

Wenn Sie das Projekt lokal bauen möchten, führen Sie einmal aus:

### Mit installiertem Gradle:
```bash
gradle wrapper
```

### Oder mit Android Studio:
Android Studio generiert die Wrapper-Dateien automatisch beim ersten Sync.

### Oder manuell:
1. Laden Sie Gradle 8.2 herunter: https://gradle.org/releases/
2. Extrahieren Sie es
3. Führen Sie aus:
   ```bash
   /path/to/gradle-8.2/bin/gradle wrapper
   ```

Dies erstellt:
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties` (bereits vorhanden)

## Für GitHub Actions

Die GitHub Actions Workflow verwendet `gradle/gradle-build-action@v2`, welches Gradle automatisch herunterlädt und einrichtet. Das Wrapper JAR wird nicht benötigt.

## Hinweis

Das `gradle-wrapper.jar` ist absichtlich nicht im Repository, da:
1. Es ein Binary ist (~60 KB)
2. Es automatisch generiert/heruntergeladen werden kann
3. GitHub Actions es nicht benötigt
