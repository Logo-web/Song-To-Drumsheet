# 🚀 Backend-Deployment für automatischen YouTube-Download

Mit diesem Backend-Service wird der YouTube-Download **vollautomatisch** ohne manuelle Schritte!

## ✨ Was ändert sich?

### **Vorher:**
1. YouTube-Link eingeben
2. Anleitung befolgen
3. Manuell auf ytmp3.nu gehen
4. Herunterladen
5. Datei hochladen

### **Nachher:**
1. YouTube-Link eingeben ✅
2. Klick auf "Laden" ✅
3. **FERTIG!** 🎉 (alles automatisch)

---

## 📦 Deployment-Optionen

### **Option 1: Vercel (Empfohlen - Kostenlos!)**

Vercel ist perfekt für Serverless Functions und **komplett kostenlos** für diesen Use-Case!

#### Schritt 1: Vercel-Account erstellen

1. Gehen Sie zu: https://vercel.com/signup
2. Registrieren Sie sich mit GitHub
3. Bestätigen Sie Ihre E-Mail

#### Schritt 2: Repository verbinden

1. Klicken Sie auf **"New Project"**
2. Wählen Sie Ihr GitHub-Repository: `Song-To-Drumsheet`
3. Klicken Sie auf **"Import"**

#### Schritt 3: Konfiguration

Vercel erkennt automatisch die Konfiguration aus `vercel.json`!

- **Framework Preset:** Other
- **Root Directory:** `./`
- **Build Command:** (leer lassen)
- **Output Directory:** `web`

#### Schritt 4: Deployen

1. Klicken Sie auf **"Deploy"**
2. Warten Sie 2-3 Minuten
3. **Fertig!** 🎉

Ihre API ist jetzt live unter:
```
https://song-to-drumsheet.vercel.app/api/youtube
```

#### Schritt 5: Web-App aktualisieren

Die Web-App erkennt automatisch, dass sie auf GitHub Pages läuft und nutzt die Vercel-API!

Keine weiteren Änderungen nötig! ✅

---

### **Option 2: Railway (Alternative)**

Railway ist eine weitere kostenlose Option:

1. Gehen Sie zu: https://railway.app
2. "Start a New Project" → "Deploy from GitHub repo"
3. Wählen Sie Ihr Repository
4. Railway deployt automatisch!

---

### **Option 3: Heroku (Traditionell)**

Für Heroku brauchen Sie zusätzlich eine `Procfile`:

```
web: node api/server.js
```

Und eine `api/server.js`:
```javascript
const express = require('express');
const app = express();
const handler = require('./youtube');

app.get('/api/youtube', handler);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
```

---

## 🔧 Lokales Testen

Bevor Sie deployen, können Sie lokal testen:

### Schritt 1: Installation

```bash
cd api
npm install
```

### Schritt 2: Vercel CLI installieren

```bash
npm install -g vercel
```

### Schritt 3: Lokal starten

```bash
vercel dev
```

Die API läuft dann auf `http://localhost:3000`

### Schritt 4: Testen

```bash
curl "http://localhost:3000/api/youtube?url=https://www.youtube.com/watch?v=dQw4w9WgXcQ"
```

---

## ⚙️ Wie es funktioniert

### 1. **Backend API** (`api/youtube.js`)

```javascript
// Nimmt YouTube-URL entgegen
// Extrahiert Audio mit ytdl-core
// Gibt Audio-URL zurück
```

### 2. **Web-App** (`web/app.js`)

```javascript
// Sendet YouTube-URL an API
// Lädt Audio von URL herunter
// Analysiert Audio direkt im Browser
```

### 3. **Vercel Serverless Function**

- Läuft nur wenn benötigt (pay-per-use)
- Automatisches Scaling
- Keine Server-Wartung
- CORS automatisch konfiguriert

---

## 📊 API-Nutzung

### Endpunkt:

```
GET /api/youtube?url={youtube_url}
```

### Beispiel-Request:

```bash
curl "https://song-to-drumsheet.vercel.app/api/youtube?url=https://www.youtube.com/watch?v=dQw4w9WgXcQ"
```

### Beispiel-Response:

```json
{
  "success": true,
  "title": "Rick Astley - Never Gonna Give You Up",
  "duration": 212,
  "audioUrl": "https://...",
  "format": {
    "container": "mp4",
    "codecs": "mp4a.40.2",
    "bitrate": 128
  }
}
```

---

## 🔐 Sicherheit & Limits

### Rate Limiting

Vercel hat eingebaute Limits:
- **Hobby Plan:** 100GB Bandwidth/Monat
- **Function Execution:** 100 Stunden/Monat
- **Invocations:** Unbegrenzt

Das reicht für **Tausende** Anfragen pro Monat!

### Empfehlungen:

1. **Environment Variables** für Secrets
2. **API Keys** für Rate Limiting (optional)
3. **Caching** für beliebte Videos

---

## ⚠️ Rechtliche Hinweise

**Wichtig:** Beachten Sie YouTube's Terms of Service:

- ✅ Erlaubt: Persönliche, nicht-kommerzielle Nutzung
- ❌ Nicht erlaubt: Massen-Downloads, Weitergabe
- 💡 Hinweis: YouTube Premium erlaubt offiziellen Download

**Empfehlung:** Fügen Sie einen Disclaimer hinzu!

---

## 🎯 Deployment-Checklist

- [ ] Vercel-Account erstellt
- [ ] Repository mit Vercel verbunden
- [ ] Projekt deployt
- [ ] API-URL funktioniert (testen!)
- [ ] Web-App auf GitHub Pages
- [ ] YouTube-Download funktioniert automatisch
- [ ] Fallback zu manuellen Anweisungen funktioniert

---

## 🆘 Troubleshooting

### "API request failed"
→ Prüfen Sie, ob Vercel-Deployment erfolgreich war
→ Checken Sie die Logs in Vercel Dashboard

### "Failed to download audio"
→ Manche Videos sind geo-blockiert
→ Prüfen Sie YouTube-Verfügbarkeit

### "CORS error"
→ Vercel sollte CORS automatisch handeln
→ Prüfen Sie `vercel.json` Konfiguration

### Rate Limit erreicht
→ Warten Sie oder upgraden Sie Vercel Plan
→ Implementieren Sie Caching

---

## 📈 Monitoring

### Vercel Dashboard:

- **Analytics:** Sehen Sie Anfragen in Echtzeit
- **Logs:** Debuggen Sie Fehler
- **Performance:** Überwachen Sie Latenz

Zugriff: https://vercel.com/dashboard

---

## 🚀 Los geht's!

**Schnellstart in 5 Minuten:**

1. Vercel-Account erstellen
2. Repository importieren
3. "Deploy" klicken
4. Warten
5. **Fertig!** ✅

Ihre Nutzer können jetzt YouTube-Links **automatisch** laden! 🎉

---

## 📞 Support

Bei Problemen:
- **Vercel Docs:** https://vercel.com/docs
- **ytdl-core Docs:** https://github.com/fent/node-ytdl-core
- **GitHub Issues:** Erstellen Sie ein Issue im Repository

---

**Viel Erfolg beim Deployment!** 🚀
