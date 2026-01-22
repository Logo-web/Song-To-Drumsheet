# 🚀 App JETZT auf Handy bauen - mit Termux

## Was ist Termux?
Termux ist eine Terminal-App für Android, mit der Sie Apps direkt auf Ihrem Handy kompilieren können.

---

## ⚡ Schnellanleitung

### Schritt 1: Termux installieren (5 Minuten)

1. **F-Droid installieren**:
   - Öffnen Sie Browser auf Ihrem Handy
   - Gehen Sie zu: **https://f-droid.org**
   - Klicken Sie auf "Download F-Droid"
   - Installieren Sie F-Droid APK

2. **Termux installieren**:
   - Öffnen Sie F-Droid App
   - Suchen Sie nach "Termux"
   - Installieren Sie "Termux"

### Schritt 2: In Termux die App bauen (15-20 Minuten)

Öffnen Sie Termux und kopieren Sie diese Befehle **einen nach dem anderen**:

```bash
# System aktualisieren (dauert 2-3 Minuten)
pkg update && pkg upgrade -y

# Werkzeuge installieren (dauert 3-5 Minuten)
pkg install -y git openjdk-17

# Speicherzugriff erlauben
termux-setup-storage
# Wenn Popup kommt: "Erlauben" klicken!

# App-Code herunterladen (dauert 1 Minute)
cd ~
git clone https://github.com/Logo-web/Song-To-Drumsheet.git
cd Song-To-Drumsheet
git checkout claude/drum-notes-generator-gEi7i

# Gradle ausführbar machen
chmod +x gradlew

# APK BAUEN! (dauert 10-15 Minuten beim ersten Mal)
./gradlew assembleDebug

# APK in Downloads kopieren
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/debug/app-debug.apk ~/storage/downloads/Song-To-Drumsheet.apk

echo "✅ FERTIG! APK ist jetzt in Downloads/"
```

### Schritt 3: APK installieren (1 Minute)

1. Öffnen Sie **Datei-Manager** oder **Downloads** App
2. Suchen Sie nach **Song-To-Drumsheet.apk**
3. Tippen Sie darauf
4. Erlauben Sie "Installation aus unbekannten Quellen" (falls gefragt)
5. Tippen Sie auf **"Installieren"**
6. **FERTIG!** 🎉

---

## 💾 Speicherplatz benötigt

- Termux: ~10 MB
- Build-Werkzeuge: ~300 MB
- App-Quellcode: ~20 MB
- Gesamt: **ca. 330 MB**

---

## ⏱️ Zeitbedarf

- Termux installieren: **5 Min**
- Befehle eingeben: **5 Min**
- Warten auf Build: **10-15 Min**
- **Gesamt: ca. 20-25 Minuten**

---

## 🆘 Häufige Probleme

### "Command not found"
→ Haben Sie alle `pkg install` Befehle ausgeführt?

### "Permission denied"
→ Führen Sie aus: `chmod +x gradlew`

### Build dauert sehr lange
→ Das ist normal beim ersten Mal! Android lädt alle Bibliotheken herunter.

### "No space left"
→ Sie brauchen mindestens 500 MB freien Speicher

### APK nicht in Downloads
→ Prüfen Sie mit: `ls ~/storage/downloads/`

---

## 📝 Tipp: Befehle kopieren

So kopieren Sie Befehle in Termux:
1. Markieren Sie den Text im Browser (lange drücken)
2. Kopieren Sie ihn
3. In Termux: Tippen Sie lange auf den Bildschirm
4. Wählen Sie "Paste"
5. Enter drücken

---

## ✨ Alternativ: Auf fertigen Build warten

Falls Ihnen das zu kompliziert ist:

**Option A**: Bitten Sie jemanden mit PC, die APK für Sie zu bauen

**Option B**: Warten Sie, bis ich einen fertigen Release erstelle
- Der Pull Request muss gemerged werden
- Dann läuft GitHub Actions automatisch
- Dann gibt es einen Download-Link

---

## 🎯 Empfehlung für Sie

**Wenn Sie technisch etwas versiert sind**: → Termux nutzen (20 Minuten)

**Wenn nicht**: → Jemanden mit PC fragen oder auf Release warten

---

## Need Help?

Wenn Sie bei einem Schritt nicht weiterkommen, sagen Sie mir Bescheid!
Ich helfe Ihnen gerne weiter. 😊
