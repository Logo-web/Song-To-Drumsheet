package com.songtodrumsheet.app.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import com.songtodrumsheet.app.model.DrumSheet
import com.songtodrumsheet.app.model.DrumType
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Handles exporting drum sheets to various formats
 */
class ExportManager(private val context: Context) {

    /**
     * Export drum sheet as PDF
     */
    suspend fun exportAsPDF(drumSheet: DrumSheet, fileName: String): ExportResult = withContext(Dispatchers.IO) {
        try {
            // Create bitmap of drum sheet
            val bitmap = createDrumSheetBitmap(drumSheet)

            // Create PDF document
            val document = PDDocument()
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)

            val contentStream = PDPageContentStream(document, page)

            // Add bitmap to PDF
            val pdImage = JPEGFactory.createFromImage(document, bitmap)
            val scale = PDRectangle.A4.width / bitmap.width.toFloat()
            val scaledHeight = bitmap.height * scale

            contentStream.drawImage(
                pdImage,
                0f,
                PDRectangle.A4.height - scaledHeight,
                PDRectangle.A4.width,
                scaledHeight
            )

            contentStream.close()

            // Save to file
            val outputDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "DrumSheets"
            )
            outputDir.mkdirs()

            val outputFile = File(outputDir, "$fileName.pdf")
            FileOutputStream(outputFile).use { outputStream ->
                document.save(outputStream)
            }
            document.close()

            ExportResult(
                success = true,
                filePath = outputFile.absolutePath
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ExportResult(
                success = false,
                error = e.message
            )
        }
    }

    /**
     * Export drum sheet as PNG image
     */
    suspend fun exportAsPNG(drumSheet: DrumSheet, fileName: String): ExportResult = withContext(Dispatchers.IO) {
        try {
            val bitmap = createDrumSheetBitmap(drumSheet)

            val outputDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "DrumSheets"
            )
            outputDir.mkdirs()

            val outputFile = File(outputDir, "$fileName.png")
            FileOutputStream(outputFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            ExportResult(
                success = true,
                filePath = outputFile.absolutePath
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ExportResult(
                success = false,
                error = e.message
            )
        }
    }

    /**
     * Create a bitmap image of the drum sheet
     */
    private fun createDrumSheetBitmap(drumSheet: DrumSheet): Bitmap {
        val width = 2100 // A4 width in pixels at 300 DPI
        val height = 2970 // A4 height in pixels at 300 DPI

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 3f
        }

        // Draw header
        paint.textSize = 48f
        paint.color = Color.BLACK
        canvas.drawText("Drum Sheet - BPM: ${drumSheet.bpm}", 50f, 100f, paint)

        // Draw staff
        val staffTop = 200f
        val staffHeight = height - 400f
        val lineSpacing = staffHeight / 6

        paint.strokeWidth = 2f
        for (i in 1..5) {
            val y = staffTop + lineSpacing * i
            canvas.drawLine(50f, y, width - 50f.toFloat(), y, paint)
        }

        // Draw measure lines
        val msPerBeat = 60000f / drumSheet.bpm
        val msPerMeasure = msPerBeat * drumSheet.timeSignature.beatsPerMeasure
        val pixelsPerMs = (width - 100f) / drumSheet.durationMs

        var currentTime = 0f
        paint.color = Color.GRAY
        paint.strokeWidth = 1f
        while (currentTime <= drumSheet.durationMs) {
            val x = 50f + currentTime * pixelsPerMs
            canvas.drawLine(x, staffTop, x, staffTop + staffHeight, paint)
            currentTime += msPerMeasure
        }

        // Draw notes
        drumSheet.notes.forEach { note ->
            val x = 50f + note.timeInMs * pixelsPerMs
            val (y, color) = getDrumNotePositionForBitmap(note.drumType, staffTop, lineSpacing)

            paint.color = color
            paint.style = Paint.Style.FILL
            val radius = 12f + (note.velocity * 6f)
            canvas.drawCircle(x, y, radius, paint)

            // Draw stem
            if (note.drumType != DrumType.HI_HAT) {
                paint.strokeWidth = 3f
                canvas.drawLine(x, y, x, y - 50f, paint)
            }
        }

        return bitmap
    }

    /**
     * Get position and color for drum notation in bitmap
     */
    private fun getDrumNotePositionForBitmap(
        drumType: DrumType,
        staffTop: Float,
        lineSpacing: Float
    ): Pair<Float, Int> {
        return when (drumType) {
            DrumType.CRASH -> Pair(staffTop + lineSpacing * 0.5f, Color.rgb(255, 107, 107))
            DrumType.RIDE -> Pair(staffTop + lineSpacing * 1f, Color.rgb(78, 205, 196))
            DrumType.HI_HAT -> Pair(staffTop + lineSpacing * 1.5f, Color.rgb(149, 225, 211))
            DrumType.TOM_HIGH -> Pair(staffTop + lineSpacing * 2f, Color.rgb(255, 230, 109))
            DrumType.SNARE -> Pair(staffTop + lineSpacing * 3f, Color.rgb(255, 107, 157))
            DrumType.TOM_MID -> Pair(staffTop + lineSpacing * 3.5f, Color.rgb(255, 165, 0))
            DrumType.TOM_LOW -> Pair(staffTop + lineSpacing * 4f, Color.rgb(255, 140, 66))
            DrumType.KICK -> Pair(staffTop + lineSpacing * 5f, Color.rgb(108, 92, 231))
        }
    }

    data class ExportResult(
        val success: Boolean,
        val filePath: String? = null,
        val error: String? = null
    )
}
