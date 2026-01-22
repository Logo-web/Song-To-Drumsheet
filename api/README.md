# 🎵 Song-To-Drumsheet API

Backend API for automatic YouTube audio extraction.

## 📋 Endpoints

### GET `/api/youtube`

Extract audio URL from YouTube video.

#### Parameters:
- `url` (required): YouTube video URL

#### Example Request:
```bash
curl "https://your-domain.vercel.app/api/youtube?url=https://www.youtube.com/watch?v=dQw4w9WgXcQ"
```

#### Example Response:
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

#### Error Response:
```json
{
  "error": "Failed to process YouTube video",
  "message": "Video unavailable"
}
```

## 🚀 Deployment

### Vercel (Recommended)

1. Connect your GitHub repository to Vercel
2. Vercel automatically detects the configuration from `vercel.json`
3. Deploy!

### Local Development

```bash
cd api
npm install
npm install -g vercel
vercel dev
```

API will be available at `http://localhost:3000`

## 📦 Dependencies

- `ytdl-core`: YouTube video downloader

## ⚠️ Legal Notice

This API is for personal, non-commercial use only. Please respect YouTube's Terms of Service and copyright laws.

## 🔧 Configuration

All configuration is handled in `vercel.json`:
- CORS headers
- Function memory and timeout
- API routes

## 📊 Limits

### Vercel Hobby Plan:
- 100GB Bandwidth/month
- 100 hours Function Execution/month
- Unlimited Invocations

Enough for thousands of requests per month!

## 🆘 Troubleshooting

### "Video unavailable"
- Video might be geo-blocked
- Video might be age-restricted
- Check if video exists

### "No audio formats found"
- YouTube might have changed format structure
- Update ytdl-core: `npm update ytdl-core`

### CORS errors
- Check `vercel.json` configuration
- Headers should be properly set

## 📖 More Information

- [Vercel Documentation](https://vercel.com/docs)
- [ytdl-core GitHub](https://github.com/fent/node-ytdl-core)
- [Deployment Guide](../BACKEND-DEPLOYMENT.md)
