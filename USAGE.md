# Benutzerhandbuch / User Guide

## Song to Drumsheet App

Diese App analysiert Musikdateien und generiert automatisch Schlagzeugnoten.

This app analyzes music files and automatically generates drum sheet music.

---

## Erste Schritte / Getting Started

### 1. Installation

1. Laden Sie die APK-Datei herunter / Download the APK file
2. Installieren Sie die App auf Ihrem Android-Gerät / Install the app on your Android device
3. Erteilen Sie die erforderlichen Berechtigungen / Grant required permissions:
   - Zugriff auf Medien/Dateien / Access to media/files

### 2. Berechtigungen / Permissions

Beim ersten Start fragt die App nach folgenden Berechtigungen / On first launch, the app requests the following permissions:

- **Medien/Dateien lesen** (READ_MEDIA_AUDIO): Zum Laden von Audiodateien / To load audio files
- **Speicher** (WRITE_EXTERNAL_STORAGE): Zum Exportieren von Noten / To export sheet music

---

## Verwendung / Usage

### Schritt 1: Audiodatei auswählen / Step 1: Select Audio File

1. Öffnen Sie die App / Open the app
2. Tippen Sie auf **"Audiodatei auswählen"** / Tap **"Select Audio File"**
3. Wählen Sie eine Musikdatei von Ihrem Gerät / Choose a music file from your device

**Unterstützte Formate / Supported Formats:**
- MP3
- WAV
- OGG
- FLAC

### Schritt 2: Lied analysieren / Step 2: Analyze Song

1. Nach der Dateiauswahl wird der Dateiname angezeigt / After selecting a file, the filename will be displayed
2. Tippen Sie auf **"Jetzt analysieren"** / Tap **"Analyze Now"**
3. Warten Sie, während die App das Lied analysiert / Wait while the app analyzes the song
   - Dies kann 10-60 Sekunden dauern / This may take 10-60 seconds
   - Die App erkennt automatisch BPM und Schläge / The app automatically detects BPM and beats

### Schritt 3: Noten anzeigen / Step 3: View Sheet Music

Nach der Analyse sehen Sie / After analysis, you'll see:

- **BPM** (Beats pro Minute / Beats per minute)
- **Taktart** (z.B. 4/4) / **Time signature** (e.g., 4/4)
- **Anzahl der erkannten Noten** / **Number of detected notes**
- **Visuelle Darstellung** der Schlagzeugnoten / **Visual representation** of drum sheet music

#### Noten lesen / Reading the Sheet Music

Die Noten sind farbcodiert / The notes are color-coded:

🔵 **Kick (Bass Drum)** - Violett / Purple
💗 **Snare** - Pink
💚 **Hi-Hat** - Türkis / Turquoise
💛 **Tom** - Gelb / Yellow
🔴 **Crash** - Rot / Red
💙 **Ride** - Cyan

Die **Größe der Note** zeigt die Intensität / The **size of the note** indicates intensity:
- Größere Noten = lauter / Larger notes = louder
- Kleinere Noten = leiser / Smaller notes = quieter

### Schritt 4: Exportieren / Step 4: Export

Sie können die Noten exportieren als / You can export the sheet music as:

#### Als PDF exportieren / Export as PDF
1. Tippen Sie auf **"Als PDF"** / Tap **"As PDF"**
2. Die Datei wird gespeichert in / The file will be saved to:
   ```
   Dokumente/DrumSheets/drumsheet_[timestamp].pdf
   ```

#### Als PNG exportieren / Export as PNG
1. Tippen Sie auf **"Als PNG"** / Tap **"As PNG"**
2. Die Datei wird gespeichert in / The file will be saved to:
   ```
   Bilder/DrumSheets/drumsheet_[timestamp].png
   ```

---

## Tipps & Tricks

### Für beste Ergebnisse / For Best Results

✅ **Verwenden Sie klare Aufnahmen** / Use clear recordings
- Weniger Hintergrundgeräusche = bessere Erkennung / Less background noise = better detection

✅ **Wählen Sie Songs mit deutlichem Schlagzeug** / Choose songs with prominent drums
- Rock, Pop, Hip-Hop funktionieren oft gut / Rock, pop, hip-hop often work well
- Orchestrale Musik kann schwieriger sein / Orchestral music may be more challenging

✅ **Kürzere Clips für Tests** / Shorter clips for testing
- Beginnen Sie mit 30-60 Sekunden / Start with 30-60 seconds
- Längere Songs brauchen mehr Zeit / Longer songs take more time

### Häufige Fragen / FAQ

**Q: Die Analyse dauert sehr lange / Analysis takes too long**
- A: Längere Songs benötigen mehr Zeit. Versuchen Sie es mit kürzeren Ausschnitten / Longer songs need more time. Try shorter excerpts

**Q: Die erkannten Noten stimmen nicht / The detected notes are incorrect**
- A: Die Genauigkeit hängt von der Audioqualität ab. Die App nutzt Algorithmen zur Beat-Erkennung, die nicht perfekt sind / Accuracy depends on audio quality. The app uses beat detection algorithms that aren't perfect

**Q: Kann ich die Noten bearbeiten? / Can I edit the notes?**
- A: Aktuell nicht in der App. Exportieren Sie als PDF und bearbeiten Sie extern / Not currently in the app. Export as PDF and edit externally

**Q: Welche Formate werden unterstützt? / What formats are supported?**
- A: MP3, WAV, OGG, und FLAC / MP3, WAV, OGG, and FLAC

**Q: Warum braucht die App Speicherzugriff? / Why does the app need storage access?**
- A: Zum Lesen von Audiodateien und Exportieren von Noten / To read audio files and export sheet music

---

## Funktionsweise / How It Works

### Technischer Überblick / Technical Overview

1. **Audio-Analyse** / Audio Analysis
   - Die App verwendet TarsosDSP zur Audioanalyse / The app uses TarsosDSP for audio analysis
   - Erkennt Onsets (Anfänge von Tönen) / Detects onsets (beginnings of sounds)
   - Berechnet BPM automatisch / Calculates BPM automatically

2. **Beat-Klassifizierung** / Beat Classification
   - Analysiert Energie und Frequenz / Analyzes energy and frequency
   - Ordnet Schläge zu Schlagzeuginstrumenten zu / Maps beats to drum instruments
   - Kick: Starke Schläge auf 1 und 3 / Strong beats on 1 and 3
   - Snare: Mittlere Schläge auf 2 und 4 / Medium beats on 2 and 4
   - Hi-Hat: Kontinuierliches Muster / Continuous pattern

3. **Noten-Generierung** / Note Generation
   - Erstellt Notation basierend auf erkannten Beats / Creates notation based on detected beats
   - Vereinfacht zu dichte Muster / Simplifies overly dense patterns
   - Fügt Hi-Hat-Muster hinzu / Adds hi-hat patterns

4. **Visualisierung** / Visualization
   - Zeichnet 5-Linien-System / Draws 5-line staff
   - Positioniert Noten nach Instrumententyp / Positions notes by instrument type
   - Zeigt Intensität durch Größe / Shows intensity through size

---

## Support

Bei Problemen oder Fragen / For issues or questions:
- GitHub Issues: [Repository Link]
- Email: [Kontakt Email]

---

## Lizenz / License

Siehe LICENSE-Datei im Repository / See LICENSE file in repository
