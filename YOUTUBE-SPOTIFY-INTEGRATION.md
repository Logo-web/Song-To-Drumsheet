# 🎵 YouTube & Spotify Integration

## 🎯 Aktuelle Implementierung

Die Web-App unterstützt jetzt URL-Eingabe für:
- ✅ YouTube Links
- ✅ Spotify Links

## ⚙️ Wie es funktioniert

### **Option 1: Direkter Download (benötigt Backend)**

Für direkten Audio-Download von YouTube/Spotify wird ein Backend-Service benötigt, da:
- Browser CORS-Einschränkungen haben
- YouTube/Spotify APIs keinen direkten Audio-Stream bieten
- Client-side Downloads nicht möglich sind

### **Option 2: Download-Anleitung (Aktuell implementiert)**

Die App zeigt dem Nutzer eine Anleitung, wie er:
1. YouTube/Spotify Audio extern herunterladen kann
2. Die Datei dann in der App hochladen kann

## 🚀 Backend-Service für vollautomatischen Download

Um vollautomatische Downloads zu ermöglichen, können Sie einen einfachen Backend-Service einrichten:

### Node.js Backend-Beispiel:

```javascript
// server.js
const express = require('express');
const ytdl = require('ytdl-core');
const cors = require('cors');
const app = express();

app.use(cors());

app.get('/api/youtube', async (req, res) => {
    const { url } = req.query;

    try {
        const info = await ytdl.getInfo(url);
        const audioFormats = ytdl.filterFormats(info.formats, 'audioonly');

        res.json({
            title: info.videoDetails.title,
            audioUrl: audioFormats[0].url
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

app.listen(3000, () => {
    console.log('API läuft auf Port 3000');
});
```

### Python (Flask) Backend-Beispiel:

```python
from flask import Flask, request, jsonify
from flask_cors import CORS
import yt_dlp

app = Flask(__name__)
CORS(app)

@app.route('/api/youtube')
def download_youtube():
    url = request.args.get('url')

    ydl_opts = {
        'format': 'bestaudio/best',
        'quiet': True
    }

    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        info = ydl.extract_info(url, download=False)
        return jsonify({
            'title': info['title'],
            'audioUrl': info['url']
        })

if __name__ == '__main__':
    app.run(port=3000)
```

## 📦 Deployment-Optionen

### 1. **Vercel / Netlify Functions** (Serverless)

Erstellen Sie eine Serverless Function:

```javascript
// api/youtube.js
const ytdl = require('ytdl-core');

module.exports = async (req, res) => {
    const { url } = req.query;

    try {
        const info = await ytdl.getInfo(url);
        const audioFormats = ytdl.filterFormats(info.formats, 'audioonly');

        res.json({
            title: info.videoDetails.title,
            audioUrl: audioFormats[0].url
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
```

### 2. **Heroku / Railway** (Container)

Deployen Sie den Node.js/Python Service als Container.

### 3. **Öffentliche APIs** (Drittanbieter)

Nutzen Sie existierende APIs:
- `y2mate.com API`
- `rapidapi.com` YouTube Downloader APIs
- `youtube-dl` als Service

## 🔧 Integration in die Web-App

Update die API-URL in `app.js`:

```javascript
async loadFromYouTube(url) {
    const apiUrl = `https://ihre-domain.vercel.app/api/youtube?url=${encodeURIComponent(url)}`;
    // Rest der Logik...
}
```

## ⚖️ Rechtliche Hinweise

**Wichtig**: Beachten Sie beim Herunterladen von YouTube/Spotify-Inhalten:

- ✅ Erlaubt: Persönliche, nicht-kommerzielle Nutzung
- ❌ Nicht erlaubt: Öffentliche Weitergabe, kommerzieller Einsatz
- ⚠️ YouTube TOS: Downloads nur mit Premium-Account offiziell erlaubt
- ⚠️ Spotify: Kein direkter Download erlaubt (nur Streaming)

**Empfehlung**: Fügen Sie einen Disclaimer hinzu:

```html
<div class="disclaimer">
    <p><small>⚠️ Bitte beachten Sie Copyright-Bestimmungen.
    Verwenden Sie diese Funktion nur für eigene Musik oder
    mit entsprechenden Rechten.</small></p>
</div>
```

## 🎯 Empfohlener Workflow

### Für normale Nutzer:
1. YouTube/Spotify Link eingeben
2. Anleitung zum Download befolgen
3. Datei in die App hochladen

### Mit Backend-Service:
1. YouTube/Spotify Link eingeben
2. Automatischer Download
3. Sofortige Analyse

## 📊 Feature-Vergleich

| Feature | Ohne Backend | Mit Backend |
|---------|--------------|-------------|
| Datei-Upload | ✅ | ✅ |
| YouTube URL | ⚠️ (Anleitung) | ✅ (Automatisch) |
| Spotify URL | ⚠️ (Anleitung) | ⚠️ (Spotify blockiert) |
| Privatsphäre | ✅ 100% lokal | ⚠️ Audio geht durch Server |
| Geschwindigkeit | Langsamer | Schneller |
| Setup | Keine | Backend nötig |

## 🛠️ Nächste Schritte

Für vollständige YouTube/Spotify-Integration:

1. **Backend deployen** (Vercel/Netlify Functions)
2. **API-URL in app.js anpassen**
3. **Rechtlichen Disclaimer hinzufügen**
4. **Rate Limiting implementieren**
5. **Caching für häufige Songs**

## 📖 Weitere Ressourcen

- [ytdl-core](https://github.com/fent/node-ytdl-core) - YouTube Downloader
- [yt-dlp](https://github.com/yt-dlp/yt-dlp) - Universal Video Downloader
- [spotdl](https://github.com/spotDL/spotify-downloader) - Spotify Downloader
- [Vercel Functions](https://vercel.com/docs/functions) - Serverless Functions
- [YouTube API](https://developers.google.com/youtube/v3) - Offizielle API

---

**Status**:
- ✅ UI implementiert
- ✅ Tab-Wechsel funktioniert
- ✅ URL-Erkennung implementiert
- ⏳ Backend-Service optional (Anleitung bereitgestellt)
