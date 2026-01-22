# Schnellstart-Anleitung: App auf dem Handy installieren

## Option 1: Mit Android Studio (Einfachste Methode)

### Schritt 1: Android Studio installieren
1. Laden Sie Android Studio herunter: https://developer.android.com/studio
2. Installieren Sie es auf Ihrem Computer (Windows/Mac/Linux)

### Schritt 2: USB-Debugging aktivieren
Auf Ihrem Android-Handy:
1. Gehen Sie zu **Einstellungen** → **Über das Telefon**
2. Tippen Sie 7x auf **Build-Nummer** (aktiviert Entwickleroptionen)
3. Gehen Sie zurück zu **Einstellungen** → **Entwickleroptionen**
4. Aktivieren Sie **USB-Debugging**

### Schritt 3: Projekt öffnen
1. Öffnen Sie Android Studio
2. Klicken Sie auf **"Open"** oder **"Open an Existing Project"**
3. Navigieren Sie zum Ordner `Song-To-Drumsheet`
4. Klicken Sie auf **"OK"**
5. Warten Sie, bis Gradle fertig synchronisiert hat (untere Statusleiste)

### Schritt 4: SDK konfigurieren
Erstellen Sie eine Datei `local.properties` im Hauptverzeichnis:

**Windows:**
```properties
sdk.dir=C\:\\Users\\IhrName\\AppData\\Local\\Android\\Sdk
```

**Mac:**
```properties
sdk.dir=/Users/IhrName/Library/Android/sdk
```

**Linux:**
```properties
sdk.dir=/home/IhrName/Android/Sdk
```

### Schritt 5: Handy verbinden und installieren
1. Verbinden Sie Ihr Handy per USB mit dem Computer
2. Entsperren Sie Ihr Handy
3. Bestätigen Sie "USB-Debugging erlauben" auf dem Handy
4. In Android Studio: Klicken Sie oben auf das **grüne Play-Symbol** ▶️
5. Wählen Sie Ihr Gerät aus der Liste
6. Klicken Sie auf **"OK"**

Die App wird jetzt auf Ihrem Handy installiert! 🎉

---

## Option 2: Mit Gradle (Kommandozeile)

Wenn Sie bereits Android Studio installiert haben:

### Schritt 1: APK bauen
```bash
cd Song-To-Drumsheet
./gradlew assembleDebug
```

### Schritt 2: APK auf Handy installieren
```bash
./gradlew installDebug
```

Die APK-Datei finden Sie hier:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## Option 3: APK manuell installieren (Ohne Android Studio)

### Schritt 1: APK bauen (auf Computer mit Android Studio)
```bash
cd Song-To-Drumsheet
./gradlew assembleDebug
```

### Schritt 2: APK auf Handy übertragen
1. Kopieren Sie die Datei `app/build/outputs/apk/debug/app-debug.apk`
2. Übertragen Sie sie per USB, E-Mail oder Cloud auf Ihr Handy

### Schritt 3: APK installieren
Auf dem Handy:
1. Öffnen Sie die Datei-Manager-App
2. Navigieren Sie zu `app-debug.apk`
3. Tippen Sie darauf
4. Wenn nötig: Aktivieren Sie **"Installation aus unbekannten Quellen"**
   - Einstellungen → Sicherheit → Unbekannte Quellen
5. Tippen Sie auf **"Installieren"**

---

## ⚠️ Häufige Probleme

### Problem: "SDK not found"
**Lösung:** Erstellen Sie die `local.properties` Datei mit dem korrekten SDK-Pfad

### Problem: "Gradle sync failed"
**Lösung:**
```bash
./gradlew clean
./gradlew build
```

### Problem: Gerät wird nicht erkannt
**Lösung:**
- USB-Debugging aktiviert?
- USB-Kabel funktioniert? (Datenkabel, nicht nur Ladekabel)
- Handy entsperrt?
- "USB-Debugging erlauben" bestätigt?

### Problem: Build dauert sehr lange
**Lösung:** Beim ersten Mal dauert es 5-10 Minuten (lädt alle Abhängigkeiten)

---

## 📱 Minimale Anforderungen

- **Android-Version:** 7.0 (API Level 24) oder höher
- **Speicherplatz:** ca. 50 MB
- **Berechtigungen:** Zugriff auf Medien/Dateien

---

## 🚀 Schnellste Methode (Zusammengefasst)

1. Android Studio installieren
2. USB-Debugging aktivieren
3. Projekt öffnen
4. Handy verbinden
5. Auf Play ▶️ klicken
6. **Fertig!** ✨
