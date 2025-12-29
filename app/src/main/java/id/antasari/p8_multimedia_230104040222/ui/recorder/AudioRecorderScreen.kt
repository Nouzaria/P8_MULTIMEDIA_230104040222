package id.antasari.p8_multimedia_230104040222.ui.recorder

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import id.antasari.p8_multimedia_230104040222.ui.Screen
import id.antasari.p8_multimedia_230104040222.util.AudioFileData
import id.antasari.p8_multimedia_230104040222.util.FileManagerUtility
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioRecorderScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val scrollState = rememberScrollState()

    // Permission
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                101
            )
        }
    }

    var isRecording by remember { mutableStateOf(false) }
    var recorder: MediaRecorder? by remember { mutableStateOf(null) }
    var outputFile by remember { mutableStateOf("") }

    // List file
    var audioFiles by remember { mutableStateOf(loadAudioFiles(context)) }

    // Rename dialog
    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }

    // Delete dialog
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    // ================= START RECORDING =================
    fun startRecording() {
        val fileName = "audio_${System.currentTimeMillis()}.mp4"
        val file = File(context.filesDir, fileName)
        outputFile = file.absolutePath

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        try {
            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)
                prepare()
                start()
                isRecording = true
            }
        } catch (e: Exception) {
            Log.e("Recorder", "Start error: ${e.message}")
            isRecording = false
        }
    }

    // ================= STOP RECORDING =================
    fun stopRecording() {
        try {
            if (isRecording) recorder?.stop()
        } catch (e: Exception) {
            Log.e("Recorder", "Stop error: ${e.message}")
        }

        recorder?.release()
        recorder = null
        isRecording = false

        val size = File(outputFile).length()

        if (size > 1500) {
            val encoded = Uri.encode(outputFile)
            onNavigate(Screen.AudioPlayer.passPath(encoded))
        }

        audioFiles = loadAudioFiles(context)
    }

    // =========================== UI ===========================

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Audio Recorder") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState) // <--- FIX LANDSCAPE + PORTRAIT SCROLL
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                if (isRecording) "Recording..." else "Ready to Record",
                color = if (isRecording) Color.Red else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            FloatingActionButton(
                onClick = {
                    if (!isRecording) startRecording()
                    else stopRecording()
                },
                shape = CircleShape,
                modifier = Modifier.size(100.dp),
                containerColor =
                    if (isRecording) Color.Red else MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (!isRecording) startRecording()
                    else stopRecording()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }

            Spacer(Modifier.height(24.dp))

            Divider()
            Spacer(Modifier.height(16.dp))

            Text(
                "Daftar Rekaman",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(12.dp))

            // ==== LIST FILE (MANUAL COLUMN) ====
            audioFiles.forEach { item ->

                FileCard(
                    data = item,
                    onPlay = {
                        val encoded = Uri.encode(item.file.absolutePath)
                        onNavigate(Screen.AudioPlayer.passPath(encoded))
                    },
                    onEdit = {
                        showRenameDialog = item.file
                        newFileName = item.file.nameWithoutExtension
                    },
                    onDelete = {
                        showDeleteDialog = item.file
                    }
                )

                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(30.dp))
        }
    }

    // ======================= RENAME DIALOG =======================
    if (showRenameDialog != null) {
        val file = showRenameDialog!!

        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Edit Nama File") },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("Nama baru (tanpa .mp4)") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.renameFile(file, "$newFileName.mp4")
                    audioFiles = loadAudioFiles(context)
                    showRenameDialog = null
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) { Text("Batal") }
            }
        )
    }

    // ======================= DELETE DIALOG =======================
    if (showDeleteDialog != null) {
        val file = showDeleteDialog!!

        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Hapus File?") },
            text = { Text(file.name) },
            confirmButton = {
                TextButton(onClick = {
                    FileManagerUtility.deleteFile(file)
                    audioFiles = loadAudioFiles(context)
                    showDeleteDialog = null
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun FileCard(
    data: AudioFileData,
    onPlay: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF7EE)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AudioFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(34.dp)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    data.file.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                "${formatDuration(data.durationMs)} • ${data.sizeText}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 46.dp)
            )

            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier.padding(start = 46.dp)) {

                Text(
                    "[Edit]",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onEdit() },
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    "[Delete]",
                    color = Color.Red,
                    modifier = Modifier.clickable { onDelete() },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun loadAudioFiles(context: android.content.Context): List<AudioFileData> {

    val audioExtensions = listOf("mp3", "wav", "m4a", "mp4")

    return FileManagerUtility.getAllAudioFiles(context)
        .filter { file ->
            file.extension.lowercase() in audioExtensions
        }
        .map { file ->
            AudioFileData(
                file,
                durationMs = FileManagerUtility.getAudioDuration(context, file),
                sizeText = FileManagerUtility.formatFileSize(file.length())
            )
        }
}

fun formatDuration(ms: Long): String {
    val sec = (ms / 1000)
    val min = sec / 60
    val s = sec % 60
    return "%02d:%02d".format(min, s)
}