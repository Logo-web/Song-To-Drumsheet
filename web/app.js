// Song to Drumsheet - Web App
// Audio analysis and drum sheet generation

class DrumSheetApp {
    constructor() {
        this.audioContext = null;
        this.audioBuffer = null;
        this.drumSheet = null;
        this.zoom = 1.0;

        this.init();
    }

    init() {
        this.setupEventListeners();
        this.audioContext = new (window.AudioContext || window.webkitAudioContext)();
    }

    setupEventListeners() {
        // Tab switching
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.switchTab(e.target.dataset.tab));
        });

        // File upload
        const fileInput = document.getElementById('audio-file');
        const uploadArea = document.getElementById('upload-area');
        const removeFileBtn = document.getElementById('remove-file');

        fileInput.addEventListener('change', (e) => this.handleFileSelect(e));

        // URL loading
        document.getElementById('load-url-btn').addEventListener('click', () => this.loadFromURL());
        document.getElementById('audio-url').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.loadFromURL();
        });

        // Drag & Drop
        uploadArea.addEventListener('dragover', (e) => {
            e.preventDefault();
            uploadArea.classList.add('drag-over');
        });

        uploadArea.addEventListener('dragleave', () => {
            uploadArea.classList.remove('drag-over');
        });

        uploadArea.addEventListener('drop', (e) => {
            e.preventDefault();
            uploadArea.classList.remove('drag-over');
            const files = e.dataTransfer.files;
            if (files.length > 0) {
                this.loadAudioFile(files[0]);
            }
        });

        removeFileBtn.addEventListener('click', () => this.removeFile());

        // Analysis
        document.getElementById('analyze-btn').addEventListener('click', () => this.analyzeAudio());

        // Zoom
        document.getElementById('zoom-in').addEventListener('click', () => this.adjustZoom(0.1));
        document.getElementById('zoom-out').addEventListener('click', () => this.adjustZoom(-0.1));

        // Export
        document.getElementById('export-png').addEventListener('click', () => this.exportAsPNG());
        document.getElementById('export-pdf').addEventListener('click', () => this.exportAsPDF());
    }

    switchTab(tab) {
        // Update tab buttons
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.tab === tab);
        });

        // Update tab content
        document.querySelectorAll('.tab-content').forEach(content => {
            content.classList.remove('active');
        });
        document.getElementById(`${tab}-tab`).classList.add('active');
    }

    handleFileSelect(event) {
        const file = event.target.files[0];
        if (file) {
            this.loadAudioFile(file);
        }
    }

    async loadFromURL() {
        const urlInput = document.getElementById('audio-url');
        const url = urlInput.value.trim();

        if (!url) {
            alert('Bitte geben Sie eine URL ein!');
            return;
        }

        // Detect service
        if (url.includes('youtube.com') || url.includes('youtu.be')) {
            await this.loadFromYouTube(url);
        } else if (url.includes('spotify.com')) {
            await this.loadFromSpotify(url);
        } else {
            alert('Nur YouTube und Spotify Links werden unterstützt!');
        }
    }

    async loadFromYouTube(url) {
        const loadingDiv = document.getElementById('url-loading');
        loadingDiv.classList.remove('hidden');

        try {
            // Extract video ID
            const videoId = this.extractYouTubeId(url);
            if (!videoId) {
                throw new Error('Ungültige YouTube URL');
            }

            // Use a CORS proxy and youtube-dl API service
            // Note: This requires a backend service or public API
            const apiUrl = `https://yt-download-api.vercel.app/api/audio?url=${encodeURIComponent(url)}`;

            const response = await fetch(apiUrl);
            if (!response.ok) {
                throw new Error('Konnte Audio nicht laden');
            }

            const data = await response.json();

            // Download audio from provided URL
            const audioResponse = await fetch(data.audioUrl);
            const audioBlob = await audioResponse.blob();

            // Convert to File object
            const file = new File([audioBlob], `youtube_${videoId}.mp3`, { type: 'audio/mpeg' });
            await this.loadAudioFile(file);

            loadingDiv.classList.add('hidden');

        } catch (error) {
            loadingDiv.classList.add('hidden');
            console.error('YouTube loading error:', error);

            // Fallback: Show instructions
            this.showDownloadInstructions('YouTube', url);
        }
    }

    async loadFromSpotify(url) {
        // Spotify doesn't provide direct audio access via API
        // Show download instructions instead
        this.showDownloadInstructions('Spotify', url);
    }

    showDownloadInstructions(service, url) {
        const message = service === 'YouTube'
            ? `YouTube-Audio kann nicht direkt geladen werden.

Bitte folgen Sie diesen Schritten:

1. Öffnen Sie: https://y2mate.com oder https://ytmp3.cc
2. Fügen Sie Ihre URL ein: ${url}
3. Laden Sie die MP3-Datei herunter
4. Laden Sie die Datei hier im "Datei hochladen" Tab hoch

Oder verwenden Sie Browser-Extensions wie "Video DownloadHelper"`
            : `Spotify-Audio kann nicht direkt geladen werden.

Bitte folgen Sie diesen Schritten:

1. Verwenden Sie ein Tool wie "Spotify Downloader" oder "Spotiload"
2. Öffnen Sie: https://spotifydown.com
3. Fügen Sie Ihre URL ein: ${url}
4. Laden Sie die MP3-Datei herunter
5. Laden Sie die Datei hier im "Datei hochladen" Tab hoch

Hinweis: Achten Sie auf Copyright-Bestimmungen!`;

        alert(message);
    }

    extractYouTubeId(url) {
        const patterns = [
            /(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&\n?#]+)/,
            /youtube\.com\/embed\/([^&\n?#]+)/
        ];

        for (const pattern of patterns) {
            const match = url.match(pattern);
            if (match) return match[1];
        }

        return null;
    }

    async loadAudioFile(file) {
        const fileInfo = document.getElementById('file-info');
        const fileName = document.getElementById('file-name');
        const uploadArea = document.getElementById('upload-area');
        const analyzeBtn = document.getElementById('analyze-btn');

        fileName.textContent = file.name;
        uploadArea.style.display = 'none';
        fileInfo.classList.remove('hidden');
        analyzeBtn.disabled = false;

        // Load audio buffer
        try {
            const arrayBuffer = await file.arrayBuffer();
            this.audioBuffer = await this.audioContext.decodeAudioData(arrayBuffer);
        } catch (error) {
            alert('Fehler beim Laden der Audiodatei: ' + error.message);
        }
    }

    removeFile() {
        document.getElementById('audio-file').value = '';
        document.getElementById('upload-area').style.display = 'block';
        document.getElementById('file-info').classList.add('hidden');
        document.getElementById('analyze-btn').disabled = true;
        this.audioBuffer = null;
        this.hideResults();
    }

    async analyzeAudio() {
        if (!this.audioBuffer) return;

        const progressContainer = document.getElementById('progress-container');
        const progressFill = document.getElementById('progress-fill');
        const progressText = document.getElementById('progress-text');
        const analyzeBtn = document.getElementById('analyze-btn');

        analyzeBtn.disabled = true;
        progressContainer.classList.remove('hidden');
        progressFill.style.width = '0%';

        try {
            // Step 1: Extract audio data
            progressText.textContent = 'Extrahiere Audio-Daten...';
            progressFill.style.width = '20%';
            await this.sleep(300);

            const audioData = this.audioBuffer.getChannelData(0);
            const sampleRate = this.audioBuffer.sampleRate;
            const duration = this.audioBuffer.duration;

            // Step 2: Detect beats
            progressText.textContent = 'Erkenne Schläge...';
            progressFill.style.width = '40%';

            const beats = this.detectBeats(audioData, sampleRate);

            // Step 3: Calculate BPM
            progressText.textContent = 'Berechne BPM...';
            progressFill.style.width = '60%';

            const bpm = this.calculateBPM(beats);

            // Step 4: Generate drum sheet
            progressText.textContent = 'Generiere Schlagzeugnoten...';
            progressFill.style.width = '80%';

            this.drumSheet = this.generateDrumSheet(beats, bpm, duration);

            // Step 5: Visualize
            progressText.textContent = 'Erstelle Visualisierung...';
            progressFill.style.width = '90%';

            await this.sleep(300);
            this.drawDrumSheet();

            progressFill.style.width = '100%';
            progressText.textContent = 'Fertig!';

            await this.sleep(500);
            progressContainer.classList.add('hidden');

            // Show results
            this.showResults(bpm, beats.length, duration);

        } catch (error) {
            alert('Fehler bei der Analyse: ' + error.message);
            console.error(error);
        } finally {
            analyzeBtn.disabled = false;
        }
    }

    detectBeats(audioData, sampleRate) {
        const beats = [];
        const windowSize = 1024;
        const hopSize = 512;
        const threshold = 0.3;

        // Calculate energy for each window
        const energyHistory = [];
        for (let i = 0; i < audioData.length - windowSize; i += hopSize) {
            let energy = 0;
            for (let j = 0; j < windowSize; j++) {
                energy += audioData[i + j] ** 2;
            }
            energy = Math.sqrt(energy / windowSize);
            energyHistory.push({ time: i / sampleRate, energy });
        }

        // Detect peaks (onsets)
        for (let i = 1; i < energyHistory.length - 1; i++) {
            const prev = energyHistory[i - 1].energy;
            const curr = energyHistory[i].energy;
            const next = energyHistory[i + 1].energy;

            // Local maximum and above threshold
            if (curr > prev && curr > next && curr > threshold) {
                beats.push({
                    timeInMs: energyHistory[i].time * 1000,
                    energy: curr,
                    frequency: 0 // Simplified
                });
            }
        }

        return beats;
    }

    calculateBPM(beats) {
        if (beats.length < 2) return 120;

        const intervals = [];
        for (let i = 1; i < beats.length && i < 50; i++) {
            intervals.push(beats[i].timeInMs - beats[i - 1].timeInMs);
        }

        if (intervals.length === 0) return 120;

        // Calculate median interval
        intervals.sort((a, b) => a - b);
        const median = intervals[Math.floor(intervals.length / 2)];

        // BPM = 60000 / interval
        const bpm = Math.round(60000 / median);
        return Math.max(40, Math.min(200, bpm));
    }

    generateDrumSheet(beats, bpm, duration) {
        const durationMs = duration * 1000;
        const notes = [];

        // Classify beats into drum types
        beats.forEach(beat => {
            const drumType = this.classifyDrumType(beat, bpm);
            notes.push({
                timeInMs: beat.timeInMs,
                drumType: drumType,
                velocity: Math.min(1, beat.energy)
            });
        });

        // Add hi-hat pattern
        const hiHatNotes = this.generateHiHatPattern(bpm, durationMs);
        notes.push(...hiHatNotes);

        // Sort by time
        notes.sort((a, b) => a.timeInMs - b.timeInMs);

        // Simplify (remove notes too close together)
        const simplified = this.simplifyNotes(notes);

        return {
            bpm: bpm,
            timeSignature: { beatsPerMeasure: 4, beatUnit: 4 },
            notes: simplified,
            durationMs: durationMs
        };
    }

    classifyDrumType(beat, bpm) {
        const beatInterval = 60000 / bpm;
        const positionInMeasure = (beat.timeInMs % (beatInterval * 4)) / beatInterval;

        // Classify based on position and energy
        if (beat.energy > 0.7 && (positionInMeasure < 0.3 || (positionInMeasure > 1.8 && positionInMeasure < 2.3))) {
            return 'KICK';
        } else if (beat.energy > 0.5 && ((positionInMeasure > 0.8 && positionInMeasure < 1.3) ||
                                         (positionInMeasure > 2.8 && positionInMeasure < 3.3))) {
            return 'SNARE';
        } else if (beat.energy > 0.9) {
            return 'CRASH';
        } else if (beat.energy > 0.6) {
            return 'TOM_MID';
        } else {
            return 'KICK';
        }
    }

    generateHiHatPattern(bpm, durationMs) {
        const notes = [];
        const eighthNoteInterval = (60000 / bpm) / 2;

        for (let time = 0; time < durationMs; time += eighthNoteInterval) {
            notes.push({
                timeInMs: time,
                drumType: 'HI_HAT',
                velocity: time % (eighthNoteInterval * 2) === 0 ? 0.7 : 0.4
            });
        }

        return notes;
    }

    simplifyNotes(notes) {
        const simplified = [];
        const minInterval = 50; // ms
        const lastTime = {};

        notes.forEach(note => {
            const last = lastTime[note.drumType] || 0;
            if (note.timeInMs - last >= minInterval) {
                simplified.push(note);
                lastTime[note.drumType] = note.timeInMs;
            }
        });

        return simplified;
    }

    drawDrumSheet() {
        const canvas = document.getElementById('drum-sheet-canvas');
        const ctx = canvas.getContext('2d');

        // Set canvas size
        const baseWidth = Math.max(1200, this.drumSheet.durationMs / 10);
        canvas.width = baseWidth * this.zoom;
        canvas.height = 500;

        // Clear canvas
        ctx.fillStyle = '#FFFFFF';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        // Draw staff
        this.drawStaff(ctx, canvas.width, canvas.height);

        // Draw measure lines
        this.drawMeasureLines(ctx, canvas.width, canvas.height);

        // Draw notes
        this.drawNotes(ctx, canvas.width, canvas.height);

        // Show sections
        document.getElementById('sheet-section').classList.remove('hidden');
        document.getElementById('export-section').classList.remove('hidden');
    }

    drawStaff(ctx, width, height) {
        const lineSpacing = height / 6;
        ctx.strokeStyle = '#000000';
        ctx.lineWidth = 2;

        // Draw 5 horizontal lines
        for (let i = 1; i <= 5; i++) {
            const y = lineSpacing * i;
            ctx.beginPath();
            ctx.moveTo(0, y);
            ctx.lineTo(width, y);
            ctx.stroke();
        }
    }

    drawMeasureLines(ctx, width, height) {
        const msPerBeat = 60000 / this.drumSheet.bpm;
        const msPerMeasure = msPerBeat * this.drumSheet.timeSignature.beatsPerMeasure;
        const pixelsPerMs = width / this.drumSheet.durationMs;

        ctx.strokeStyle = '#CCCCCC';
        ctx.lineWidth = 1;

        for (let time = 0; time <= this.drumSheet.durationMs; time += msPerMeasure) {
            const x = time * pixelsPerMs;
            ctx.beginPath();
            ctx.moveTo(x, 0);
            ctx.lineTo(x, height);
            ctx.stroke();
        }
    }

    drawNotes(ctx, width, height) {
        const lineSpacing = height / 6;
        const pixelsPerMs = width / this.drumSheet.durationMs;

        this.drumSheet.notes.forEach(note => {
            const x = note.timeInMs * pixelsPerMs;
            const [y, color] = this.getDrumNotePosition(note.drumType, lineSpacing);
            const radius = 6 + (note.velocity * 4);

            // Draw note head
            ctx.fillStyle = color;
            ctx.beginPath();
            ctx.arc(x, y, radius, 0, Math.PI * 2);
            ctx.fill();

            // Draw stem (except for hi-hat)
            if (note.drumType !== 'HI_HAT') {
                ctx.strokeStyle = color;
                ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.moveTo(x, y);
                ctx.lineTo(x, y - 30);
                ctx.stroke();
            }
        });
    }

    getDrumNotePosition(drumType, lineSpacing) {
        const positions = {
            'CRASH': [lineSpacing * 0.5, '#FF6B6B'],
            'RIDE': [lineSpacing * 1, '#4ECDC4'],
            'HI_HAT': [lineSpacing * 1.5, '#95E1D3'],
            'TOM_HIGH': [lineSpacing * 2, '#FFE66D'],
            'SNARE': [lineSpacing * 3, '#FF6B9D'],
            'TOM_MID': [lineSpacing * 3.5, '#FFA500'],
            'TOM_LOW': [lineSpacing * 4, '#FF8C42'],
            'KICK': [lineSpacing * 5, '#6C5CE7']
        };

        return positions[drumType] || [lineSpacing * 3, '#000000'];
    }

    showResults(bpm, beatsCount, duration) {
        document.getElementById('bpm-value').textContent = bpm;
        document.getElementById('beats-value').textContent = beatsCount;
        document.getElementById('duration-value').textContent = this.formatDuration(duration);
        document.getElementById('analysis-result').classList.remove('hidden');
    }

    hideResults() {
        document.getElementById('analysis-result').classList.add('hidden');
        document.getElementById('sheet-section').classList.add('hidden');
        document.getElementById('export-section').classList.add('hidden');
    }

    adjustZoom(delta) {
        this.zoom = Math.max(0.5, Math.min(3, this.zoom + delta));
        this.drawDrumSheet();
    }

    exportAsPNG() {
        const canvas = document.getElementById('drum-sheet-canvas');
        const link = document.createElement('a');
        link.download = `drumsheet_${Date.now()}.png`;
        link.href = canvas.toDataURL('image/png');
        link.click();
    }

    exportAsPDF() {
        const canvas = document.getElementById('drum-sheet-canvas');
        const imgData = canvas.toDataURL('image/png');

        const { jsPDF } = window.jspdf;
        const pdf = new jsPDF({
            orientation: 'landscape',
            unit: 'mm',
            format: 'a4'
        });

        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = pdf.internal.pageSize.getHeight();
        const imgWidth = pdfWidth - 20;
        const imgHeight = (canvas.height / canvas.width) * imgWidth;

        // Add title
        pdf.setFontSize(20);
        pdf.text(`Drum Sheet - BPM: ${this.drumSheet.bpm}`, 10, 15);

        // Add image
        pdf.addImage(imgData, 'PNG', 10, 25, imgWidth, Math.min(imgHeight, pdfHeight - 30));

        pdf.save(`drumsheet_${Date.now()}.pdf`);
    }

    formatDuration(seconds) {
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    new DrumSheetApp();
});
