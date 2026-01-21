package com.songtodrumsheet.app.generator

import com.songtodrumsheet.app.model.Beat
import com.songtodrumsheet.app.model.DrumNote
import com.songtodrumsheet.app.model.DrumSheet
import com.songtodrumsheet.app.model.DrumType
import com.songtodrumsheet.app.model.TimeSignature

/**
 * Generates drum notation from detected beats
 */
class DrumNotationGenerator {

    /**
     * Convert detected beats into drum sheet notation
     */
    fun generateDrumSheet(
        beats: List<Beat>,
        bpm: Int,
        durationMs: Long
    ): DrumSheet {
        val drumNotes = mutableListOf<DrumNote>()

        // Analyze beats and classify them into drum types
        beats.forEach { beat ->
            // Classify based on energy patterns and timing
            val drumType = classifyDrumType(beat, beats, bpm)

            drumNotes.add(
                DrumNote(
                    timeInMs = beat.timeInMs,
                    drumType = drumType,
                    velocity = beat.energy.coerceIn(0f, 1f)
                )
            )
        }

        // Add hi-hat pattern (common in most songs)
        val hiHatNotes = generateHiHatPattern(bpm, durationMs)
        drumNotes.addAll(hiHatNotes)

        // Sort all notes by time
        val sortedNotes = drumNotes.sortedBy { it.timeInMs }

        return DrumSheet(
            bpm = bpm,
            timeSignature = TimeSignature(4, 4),
            notes = sortedNotes,
            durationMs = durationMs
        )
    }

    /**
     * Classify a beat into a drum type based on its characteristics
     */
    private fun classifyDrumType(beat: Beat, allBeats: List<Beat>, bpm: Int): DrumType {
        val beatInterval = 60000L / bpm // ms per beat

        // Classify based on position in measure and energy
        val positionInMeasure = (beat.timeInMs % (beatInterval * 4)) / beatInterval.toFloat()

        return when {
            // Strong beats on 1 and 3 are likely kicks
            beat.energy > 0.7f && (positionInMeasure < 0.2f || (positionInMeasure > 1.8f && positionInMeasure < 2.2f)) -> {
                DrumType.KICK
            }
            // Medium energy beats on 2 and 4 are likely snares
            beat.energy > 0.5f && ((positionInMeasure > 0.8f && positionInMeasure < 1.2f) ||
                                    (positionInMeasure > 2.8f && positionInMeasure < 3.2f)) -> {
                DrumType.SNARE
            }
            // Very high energy might be crashes
            beat.energy > 0.9f -> DrumType.CRASH
            // Lower energy could be toms
            beat.energy > 0.6f -> DrumType.TOM_MID
            // Default to kick for strong beats
            else -> DrumType.KICK
        }
    }

    /**
     * Generate a basic hi-hat pattern
     */
    private fun generateHiHatPattern(bpm: Int, durationMs: Long): List<DrumNote> {
        val hiHatNotes = mutableListOf<DrumNote>()
        val eighthNoteInterval = (60000L / bpm) / 2 // Eighth notes

        var currentTime = 0L
        while (currentTime < durationMs) {
            hiHatNotes.add(
                DrumNote(
                    timeInMs = currentTime,
                    drumType = DrumType.HI_HAT,
                    velocity = if (currentTime % (eighthNoteInterval * 2) == 0L) 0.7f else 0.4f
                )
            )
            currentTime += eighthNoteInterval
        }

        return hiHatNotes
    }

    /**
     * Simplify drum sheet by removing notes that are too close together
     */
    fun simplifyDrumSheet(sheet: DrumSheet, minIntervalMs: Long = 50): DrumSheet {
        val simplified = mutableListOf<DrumNote>()
        var lastTimeByType = mutableMapOf<DrumType, Long>()

        sheet.notes.forEach { note ->
            val lastTime = lastTimeByType[note.drumType] ?: 0L
            if (note.timeInMs - lastTime >= minIntervalMs) {
                simplified.add(note)
                lastTimeByType[note.drumType] = note.timeInMs
            }
        }

        return sheet.copy(notes = simplified)
    }
}
