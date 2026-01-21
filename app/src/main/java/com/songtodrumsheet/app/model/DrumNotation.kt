package com.songtodrumsheet.app.model

/**
 * Represents a drum note at a specific time
 */
data class DrumNote(
    val timeInMs: Long,
    val drumType: DrumType,
    val velocity: Float // 0.0 to 1.0, represents intensity
)

/**
 * Types of drum instruments
 */
enum class DrumType {
    KICK,        // Bass Drum
    SNARE,       // Snare Drum
    HI_HAT,      // Hi-Hat
    TOM_HIGH,    // High Tom
    TOM_MID,     // Mid Tom
    TOM_LOW,     // Low Tom
    CRASH,       // Crash Cymbal
    RIDE         // Ride Cymbal
}

/**
 * Represents a complete drum sheet with metadata
 */
data class DrumSheet(
    val bpm: Int,
    val timeSignature: TimeSignature = TimeSignature(4, 4),
    val notes: List<DrumNote>,
    val durationMs: Long
)

/**
 * Time signature for the drum sheet
 */
data class TimeSignature(
    val beatsPerMeasure: Int = 4,
    val beatUnit: Int = 4 // 4 means quarter note gets the beat
)

/**
 * Represents a detected beat with its characteristics
 */
data class Beat(
    val timeInMs: Long,
    val energy: Float, // Intensity of the beat
    val frequency: Float // Frequency in Hz
)
