package com.songtodrumsheet.app.audio

import android.content.Context
import android.net.Uri
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AndroidAudioPlayer
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.onsets.ComplexOnsetDetector
import be.tarsos.dsp.onsets.OnsetHandler
import be.tarsos.dsp.util.fft.FFT
import com.songtodrumsheet.app.model.Beat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Detects beats and rhythm from audio files using TarsosDSP
 */
class BeatDetector(private val context: Context) {

    private val detectedBeats = mutableListOf<Beat>()
    private var bpm: Int = 0

    /**
     * Analyze an audio file and detect beats
     */
    suspend fun analyzeAudioFile(uri: Uri): AnalysisResult = withContext(Dispatchers.IO) {
        detectedBeats.clear()

        try {
            val dispatcher = AudioDispatcherFactory.fromPipe(
                context.contentResolver.openInputStream(uri)?.let { inputStream ->
                    // Create temporary file
                    val tempFile = createTempFile("audio", ".tmp", context.cacheDir)
                    tempFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    tempFile.absolutePath
                } ?: throw IOException("Cannot open audio file"),
                22050,
                1024,
                512
            )

            var totalEnergy = 0.0
            var frameCount = 0

            // Add onset detector for beat detection
            val onsetHandler = OnsetHandler { time, salience ->
                val timeInMs = (time * 1000).toLong()
                detectedBeats.add(
                    Beat(
                        timeInMs = timeInMs,
                        energy = salience.toFloat(),
                        frequency = 0f // Will be calculated later
                    )
                )
            }

            val complexOnsetDetector = ComplexOnsetDetector(1024, 0.3, 0.4)
            complexOnsetDetector.setHandler(onsetHandler)

            // Energy-based beat detection
            val energyProcessor = object : AudioProcessor {
                override fun process(audioEvent: AudioEvent): Boolean {
                    val buffer = audioEvent.floatBuffer
                    var energy = 0.0

                    for (i in buffer.indices) {
                        energy += buffer[i] * buffer[i]
                    }

                    energy = Math.sqrt(energy / buffer.size)
                    totalEnergy += energy
                    frameCount++

                    return true
                }

                override fun processingFinished() {}
            }

            dispatcher.addAudioProcessor(complexOnsetDetector)
            dispatcher.addAudioProcessor(energyProcessor)
            dispatcher.run()

            // Calculate BPM from detected beats
            bpm = calculateBPM(detectedBeats)

            val durationMs = if (detectedBeats.isNotEmpty()) {
                detectedBeats.maxOf { it.timeInMs }
            } else {
                0L
            }

            AnalysisResult(
                beats = detectedBeats.toList(),
                bpm = bpm,
                durationMs = durationMs,
                success = true
            )

        } catch (e: Exception) {
            e.printStackTrace()
            AnalysisResult(
                beats = emptyList(),
                bpm = 0,
                durationMs = 0,
                success = false,
                error = e.message
            )
        }
    }

    /**
     * Calculate BPM from detected beats
     */
    private fun calculateBPM(beats: List<Beat>): Int {
        if (beats.size < 2) return 120 // Default BPM

        // Calculate intervals between beats
        val intervals = mutableListOf<Long>()
        for (i in 1 until beats.size) {
            intervals.add(beats[i].timeInMs - beats[i - 1].timeInMs)
        }

        // Find most common interval (simple approach)
        val avgInterval = intervals.average()

        // BPM = 60000ms / interval
        return if (avgInterval > 0) {
            (60000 / avgInterval).toInt().coerceIn(40, 200)
        } else {
            120
        }
    }

    /**
     * Result of audio analysis
     */
    data class AnalysisResult(
        val beats: List<Beat>,
        val bpm: Int,
        val durationMs: Long,
        val success: Boolean,
        val error: String? = null
    )
}
