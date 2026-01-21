package com.songtodrumsheet.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.songtodrumsheet.app.model.DrumNote
import com.songtodrumsheet.app.model.DrumSheet
import com.songtodrumsheet.app.model.DrumType

/**
 * Composable that displays drum sheet notation
 */
@Composable
fun DrumSheetView(
    drumSheet: DrumSheet,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header with BPM info
        Text(
            text = "BPM: ${drumSheet.bpm} | ${drumSheet.timeSignature.beatsPerMeasure}/${drumSheet.timeSignature.beatUnit}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Scrollable drum notation view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
                .horizontalScroll(rememberScrollState())
        ) {
            DrumNotationCanvas(
                drumSheet = drumSheet,
                modifier = Modifier
                    .width((drumSheet.durationMs / 100).dp.coerceAtLeast(800.dp))
                    .fillMaxHeight()
            )
        }

        // Legend
        Spacer(modifier = Modifier.height(16.dp))
        DrumLegend()
    }
}

@Composable
private fun DrumNotationCanvas(
    drumSheet: DrumSheet,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        drawDrumStaff()
        drawMeasureLines(drumSheet)
        drawDrumNotes(drumSheet)
    }
}

/**
 * Draw the 5-line staff for drum notation
 */
private fun DrawScope.drawDrumStaff() {
    val staffColor = Color.Black
    val lineSpacing = size.height / 6

    // Draw 5 horizontal lines
    for (i in 1..5) {
        val y = lineSpacing * i
        drawLine(
            color = staffColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 2f
        )
    }
}

/**
 * Draw vertical measure lines
 */
private fun DrawScope.drawMeasureLines(drumSheet: DrumSheet) {
    val msPerBeat = 60000f / drumSheet.bpm
    val msPerMeasure = msPerBeat * drumSheet.timeSignature.beatsPerMeasure
    val pixelsPerMs = size.width / drumSheet.durationMs

    var currentTime = 0f
    while (currentTime <= drumSheet.durationMs) {
        val x = currentTime * pixelsPerMs
        drawLine(
            color = Color.Gray,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1f
        )
        currentTime += msPerMeasure
    }
}

/**
 * Draw drum notes on the staff
 */
private fun DrawScope.drawDrumNotes(drumSheet: DrumSheet) {
    val lineSpacing = size.height / 6
    val pixelsPerMs = size.width / drumSheet.durationMs.toFloat()

    drumSheet.notes.forEach { note ->
        val x = note.timeInMs * pixelsPerMs
        val (y, color) = getDrumNotePosition(note.drumType, lineSpacing)

        // Draw note head
        val radius = 8f + (note.velocity * 4f)
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(x, y)
        )

        // Draw stem for some drums
        if (note.drumType != DrumType.HI_HAT) {
            drawLine(
                color = color,
                start = Offset(x, y),
                end = Offset(x, y - 30f),
                strokeWidth = 2f
            )
        }
    }
}

/**
 * Get the vertical position and color for each drum type
 */
private fun getDrumNotePosition(drumType: DrumType, lineSpacing: Float): Pair<Float, Color> {
    return when (drumType) {
        DrumType.CRASH -> Pair(lineSpacing * 0.5f, Color(0xFFFF6B6B))
        DrumType.RIDE -> Pair(lineSpacing * 1f, Color(0xFF4ECDC4))
        DrumType.HI_HAT -> Pair(lineSpacing * 1.5f, Color(0xFF95E1D3))
        DrumType.TOM_HIGH -> Pair(lineSpacing * 2f, Color(0xFFFFE66D))
        DrumType.SNARE -> Pair(lineSpacing * 3f, Color(0xFFFF6B9D))
        DrumType.TOM_MID -> Pair(lineSpacing * 3.5f, Color(0xFFFFA500))
        DrumType.TOM_LOW -> Pair(lineSpacing * 4f, Color(0xFFFF8C42))
        DrumType.KICK -> Pair(lineSpacing * 5f, Color(0xFF6C5CE7))
    }
}

@Composable
private fun DrumLegend() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Legende / Legend:",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            LegendItem("Kick", Color(0xFF6C5CE7))
            LegendItem("Snare", Color(0xFFFF6B9D))
            LegendItem("Hi-Hat", Color(0xFF95E1D3))
            LegendItem("Tom", Color(0xFFFFE66D))
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            LegendItem("Crash", Color(0xFFFF6B6B))
            LegendItem("Ride", Color(0xFF4ECDC4))
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
