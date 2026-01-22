# 🌐 Song to Drumsheet - Web App

Nutzen Sie die App **direkt im Browser** - keine Installation nötig!

## 🚀 Live Demo

**Direkt ausprobieren:** [Song-To-Drumsheet.github.io](https://logo-web.github.io/Song-To-Drumsheet/web/)

## ✨ Features

- 🎵 **Audio-Upload**: MP3, WAV, OGG, FLAC
- 🔗 **YouTube & Spotify Links**: Unterstützung für externe Audio-Quellen
- 🥁 **Automatische Beat-Erkennung**
- 📊 **BPM-Berechnung**
- 🎼 **Schlagzeugnoten-Visualisierung**
- 📥 **Export als PNG oder PDF**
- 🔒 **100% Privat**: Alle Verarbeitung erfolgt lokal in Ihrem Browser

## 📱 Verwendung

### 1. Audio-Quelle wählen

**Option A: Datei hochladen**
- Klicken Sie auf "📁 Datei hochladen"
- Klicken Sie auf das Upload-Feld oder ziehen Sie eine Datei per Drag & Drop

**Option B: YouTube/Spotify Link**
- Klicken Sie auf "🔗 YouTube / Spotify Link"
- Fügen Sie einen YouTube- oder Spotify-Link ein
- Klicken Sie auf "Laden"
- Folgen Sie ggf. den Anweisungen zum Download

### 2. Analysieren
- Klicken Sie auf "Jetzt analysieren"
- Warten Sie 5-15 Sekunden (abhängig von der Länge)

### 3. Ergebnis ansehen
- Sehen Sie die generierten Schlagzeugnoten
- Zoomen Sie für bessere Ansicht

### 4. Exportieren
- Speichern Sie als PNG-Bild
- Oder als PDF-Dokument

## 🎨 Legende

Die Noten sind farbcodiert:

- 🟣 **Kick** (Bass Drum) - Violett
- 💗 **Snare** - Pink
- 💚 **Hi-Hat** - Türkis
- 💛 **Tom** - Gelb
- 🔴 **Crash** - Rot
- 🔵 **Ride** - Cyan

## 🔧 Technologie

- **Web Audio API** - Audio-Analyse
- **HTML5 Canvas** - Visualisierung
- **jsPDF** - PDF-Export
- **Vanilla JavaScript** - Keine Frameworks
- **100% Client-seitig** - Keine Server, keine Uploads

## 🌍 Browser-Kompatibilität

- ✅ Chrome / Edge (empfohlen)
- ✅ Firefox
- ✅ Safari
- ✅ Opera

**Mindestanforderungen:**
- Moderner Browser mit Web Audio API Support
- JavaScript aktiviert

## 💡 Tipps

- **Kürzere Clips**: Für schnellere Analyse verwenden Sie 30-60 Sekunden
- **Gute Audioqualität**: Bessere Qualität = bessere Erkennung
- **Deutliches Schlagzeug**: Songs mit klarem Schlagzeug funktionieren am besten

## 🆚 Web vs. Android App

| Feature | Web | Android App |
|---------|-----|-------------|
| Installation | ❌ Nicht nötig | ✅ APK installieren |
| Plattform | 🌐 Alle Geräte | 📱 Nur Android |
| Offline | ❌ Internet nötig | ✅ Offline nutzbar |
| Geschwindigkeit | 🟢 Schnell | 🟢 Sehr schnell |
| Genauigkeit | 🟢 Gut | 🟢 Sehr gut |

## 🔐 Datenschutz

**Ihre Daten bleiben bei Ihnen!**

- ✅ Keine Uploads zu Servern
- ✅ Alle Verarbeitung im Browser
- ✅ Keine Tracking-Cookies
- ✅ Open Source Code

## 📖 Wie funktioniert's?

1. **Audio-Laden**: Ihre Datei wird in den Browser geladen
2. **FFT-Analyse**: Web Audio API analysiert Frequenzen
3. **Beat-Detection**: Energie-Peaks werden als Schläge erkannt
4. **BPM-Berechnung**: Zeitintervalle werden analysiert
5. **Klassifizierung**: Schläge werden Drums zugeordnet
6. **Visualisierung**: Canvas zeichnet die Notation

## 🐛 Probleme melden

Fehler gefunden? [Issue auf GitHub erstellen](https://github.com/Logo-web/Song-To-Drumsheet/issues)

## 📄 Lizenz

Siehe [LICENSE](../LICENSE) im Haupt-Repository

## 🙏 Credits

- Beat-Detection-Algorithmus basiert auf Energie-Analyse
- UI inspiriert von modernen Web-Apps
- Icons von Heroicons

---

**Viel Spaß beim Erstellen von Schlagzeugnoten!** 🥁✨
