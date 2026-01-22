const ytdl = require('ytdl-core');

export default async function handler(req, res) {
    // Enable CORS
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

    if (req.method === 'OPTIONS') {
        return res.status(200).end();
    }

    const { url } = req.query;

    if (!url) {
        return res.status(400).json({ error: 'URL parameter is required' });
    }

    try {
        // Validate YouTube URL
        if (!ytdl.validateURL(url)) {
            return res.status(400).json({ error: 'Invalid YouTube URL' });
        }

        // Get video info
        const info = await ytdl.getInfo(url);

        // Get audio-only formats
        const audioFormats = ytdl.filterFormats(info.formats, 'audioonly');

        if (audioFormats.length === 0) {
            return res.status(404).json({ error: 'No audio formats found' });
        }

        // Get best audio quality
        const bestAudio = audioFormats.reduce((best, format) => {
            return format.audioBitrate > (best.audioBitrate || 0) ? format : best;
        }, audioFormats[0]);

        // Return audio URL and metadata
        return res.status(200).json({
            success: true,
            title: info.videoDetails.title,
            duration: parseInt(info.videoDetails.lengthSeconds),
            audioUrl: bestAudio.url,
            format: {
                container: bestAudio.container,
                codecs: bestAudio.codecs,
                bitrate: bestAudio.audioBitrate
            }
        });

    } catch (error) {
        console.error('YouTube download error:', error);
        return res.status(500).json({
            error: 'Failed to process YouTube video',
            message: error.message
        });
    }
}
