# 📱 App auf Handy installieren - OHNE PC

Es gibt mehrere Wege, die App **direkt auf Ihr Handy** zu bekommen, ohne einen PC zu benutzen!

---

## ✅ Methode 1: Fertige APK von GitHub herunterladen (Einfachste!)

### Sobald der Code gemerged ist:

1. **Öffnen Sie auf Ihrem Handy den Browser** (Chrome, Firefox, etc.)

2. **Gehen Sie zu GitHub**:
   ```
   https://github.com/Logo-web/Song-To-Drumsheet
   ```

3. **Klicken Sie auf "Releases"** (rechte Seite)
   - Oder gehen Sie direkt zu: `https://github.com/Logo-web/Song-To-Drumsheet/releases`

4. **Laden Sie `app-debug.apk` herunter**
   - Tippen Sie auf die APK-Datei
   - Download startet automatisch

5. **Installieren Sie die APK**:
   - Öffnen Sie die Download-Benachrichtigung
   - Tippen Sie auf `app-debug.apk`
   - Falls nötig: Aktivieren Sie "Installation aus unbekannten Quellen"
     - Einstellungen → Sicherheit → Unbekannte Quellen → AN
   - Tippen Sie auf **"Installieren"**

6. **Fertig!** 🎉 Die App ist jetzt auf Ihrem Handy!

---

## 🔧 Methode 2: Mit Termux bauen (Für Fortgeschrittene)

Sie können die App **direkt auf Ihrem Handy bauen** mit Termux!

### Schritt 1: Termux installieren
1. Öffnen Sie **F-Droid** App Store: https://f-droid.org/
2. Oder laden Sie direkt: https://f-droid.org/repo/com.termux_118.apk
3. Installieren Sie **Termux**

### Schritt 2: Termux einrichten
Öffnen Sie Termux und führen Sie aus:

```bash
# System aktualisieren
pkg update && pkg upgrade -y

# Benötigte Tools installieren
pkg install -y git openjdk-17 wget unzip

# Repository klonen
cd ~
git clone https://github.com/Logo-web/Song-To-Drumsheet.git
cd Song-To-Drumsheet

# Gradle Wrapper ausführbar machen
chmod +x gradlew

# APK bauen (dauert 10-20 Minuten beim ersten Mal!)
./gradlew assembleDebug
```

### Schritt 3: APK installieren
```bash
# APK finden
cd ~/Song-To-Drumsheet
ls app/build/outputs/apk/debug/

# APK in Download-Ordner kopieren
cp app/build/outputs/apk/debug/app-debug.apk ~/storage/downloads/

# Jetzt können Sie die APK installieren:
# - Öffnen Sie den Datei-Manager
# - Gehen Sie zu Downloads
# - Tippen Sie auf app-debug.apk
# - Installieren!
```

**Hinweis**: Beim ersten Build lädt Termux alle Abhängigkeiten herunter (~500 MB), das dauert!

---

## 🌐 Methode 3: GitHub Actions (Automatisch)

### Was ist das?
GitHub baut automatisch die APK für Sie, sobald der Code gepusht wird!

### So funktioniert's:

1. **Warten Sie, bis der Code gemerged ist** (Pull Request akzeptiert)

2. **GitHub Actions läuft automatisch**:
   - Geht zu: `https://github.com/Logo-web/Song-To-Drumsheet/actions`
   - Wartet, bis der Build grün ist ✅

3. **APK herunterladen**:
   - Klicken Sie auf den neuesten erfolgreichen Build
   - Scrollen Sie runter zu **"Artifacts"**
   - Klicken Sie auf **"app-debug"**
   - APK wird heruntergeladen

4. **Installieren** (wie oben beschrieben)

---

## 📲 Methode 4: Online Build-Services

Es gibt auch Online-Services, die Android-Apps bauen:

### AppCenter / Bitrise / CircleCI
Diese Services können automatisch APKs bauen, aber sie benötigen Setup.

---

## ⚠️ "Unbekannte Quellen" aktivieren

Falls Sie beim Installieren der APK eine Warnung bekommen:

### Android 8+:
1. Beim Installieren: Tippen Sie auf **"Einstellungen"**
2. Aktivieren Sie **"Aus dieser Quelle zulassen"**
3. Gehen Sie zurück und installieren Sie

### Android 7 und älter:
1. Einstellungen → Sicherheit
2. Aktivieren Sie **"Unbekannte Quellen"**
3. Installieren Sie die APK

---

## 🎯 Empfehlung

**Für normale Nutzer**: Methode 1 (Fertige APK herunterladen)
- ✅ Einfachste Methode
- ✅ Schnell
- ✅ Keine technischen Kenntnisse nötig

**Für Entwickler/Bastler**: Methode 2 (Termux)
- ✅ Volle Kontrolle
- ✅ Lernen Sie Android-Entwicklung
- ⚠️ Dauert länger
- ⚠️ Braucht technisches Verständnis

---

## 🆘 Hilfe & Probleme

### "Installation blockiert"
→ Aktivieren Sie "Unbekannte Quellen" in den Einstellungen

### "App kann nicht installiert werden"
→ Stellen Sie sicher, dass Sie Android 7.0+ haben

### "Nicht genug Speicherplatz"
→ Die App braucht ca. 50 MB freien Speicher

### "APK nicht gefunden"
→ Prüfen Sie Ihren Download-Ordner im Datei-Manager

---

## 📱 Alternative: App Store (Zukunft)

In Zukunft könnte die App auch im **Google Play Store** oder **F-Droid** veröffentlicht werden.
Dann können Sie sie einfach wie jede andere App installieren!

---

## 🎉 Los geht's!

Sobald der Code gemerged ist, können Sie die App direkt herunterladen!

**Link zum Repository**:
https://github.com/Logo-web/Song-To-Drumsheet

**Link zu Releases** (sobald verfügbar):
https://github.com/Logo-web/Song-To-Drumsheet/releases
