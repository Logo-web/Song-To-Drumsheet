package com.songtodrumsheet.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.songtodrumsheet.app.audio.BeatDetector
import com.songtodrumsheet.app.export.ExportManager
import com.songtodrumsheet.app.generator.DrumNotationGenerator
import com.songtodrumsheet.app.model.DrumSheet
import com.songtodrumsheet.app.ui.DrumSheetView
import com.songtodrumsheet.app.ui.theme.SongToDrumsheetTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions
        requestPermissions()

        setContent {
            SongToDrumsheetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrumSheetApp()
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrumSheetApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var drumSheet by remember { mutableStateOf<DrumSheet?>(null) }
    var analysisStatus by remember { mutableStateOf("") }

    val beatDetector = remember { BeatDetector(context) }
    val generator = remember { DrumNotationGenerator() }
    val exportManager = remember { ExportManager(context) }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            selectedFileName = getFileName(context, it)
            drumSheet = null
            analysisStatus = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Song to Drumsheet") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // File selection section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Schritt 1: Audiodatei auswählen",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { filePickerLauncher.launch("audio/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.FileOpen, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Audiodatei auswählen")
                    }

                    if (selectedFileName != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ausgewählt: $selectedFileName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Analysis section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Schritt 2: Lied analysieren",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            selectedFileUri?.let { uri ->
                                scope.launch {
                                    isAnalyzing = true
                                    analysisStatus = "Analysiere Audio..."

                                    val result = beatDetector.analyzeAudioFile(uri)

                                    if (result.success) {
                                        analysisStatus = "Generiere Schlagzeugnoten..."
                                        drumSheet = generator.generateDrumSheet(
                                            beats = result.beats,
                                            bpm = result.bpm,
                                            durationMs = result.durationMs
                                        )

                                        // Simplify the sheet
                                        drumSheet = drumSheet?.let { generator.simplifyDrumSheet(it) }

                                        analysisStatus = "Fertig! ${result.beats.size} Schläge erkannt"
                                        Toast.makeText(
                                            context,
                                            "Analyse abgeschlossen!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        analysisStatus = "Fehler: ${result.error}"
                                        Toast.makeText(
                                            context,
                                            "Fehler bei der Analyse",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    isAnalyzing = false
                                }
                            }
                        },
                        enabled = selectedFileUri != null && !isAnalyzing,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (isAnalyzing) "Analysiere..." else "Jetzt analysieren")
                    }

                    if (analysisStatus.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = analysisStatus,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Display drum sheet
            drumSheet?.let { sheet ->
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Schritt 3: Schlagzeugnoten",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${sheet.notes.size} Noten generiert",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DrumSheetView(drumSheet = sheet)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Export buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val fileName = "drumsheet_${System.currentTimeMillis()}"
                                        val result = exportManager.exportAsPDF(sheet, fileName)
                                        if (result.success) {
                                            Toast.makeText(
                                                context,
                                                "PDF gespeichert: ${result.filePath}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Fehler beim Export: ${result.error}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Als PDF")
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        val fileName = "drumsheet_${System.currentTimeMillis()}"
                                        val result = exportManager.exportAsPNG(sheet, fileName)
                                        if (result.success) {
                                            Toast.makeText(
                                                context,
                                                "PNG gespeichert: ${result.filePath}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Fehler beim Export: ${result.error}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Als PNG")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getFileName(context: android.content.Context, uri: Uri): String {
    var result = "Audiodatei"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                result = it.getString(nameIndex)
            }
        }
    }
    return result
}
