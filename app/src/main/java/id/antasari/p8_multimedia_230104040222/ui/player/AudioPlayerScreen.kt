package id.antasari.p8_multimedia_230104040222.ui.player

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import id.antasari.p8_multimedia_230104040222.ui.Screen
import id.antasari.p8_multimedia_230104040222.util.AudioFileData
import id.antasari.p8_multimedia_230104040222.util.FileManagerUtility
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(
    onBack: () -> Unit,
    audioPath: String
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var currentFile by remember { mutableStateOf(File(audioPath)) }
    var audioFiles by remember { mutableStateOf(loadAudioFiles(context)) }

    var showRenameDialog by remember { mutableStateOf<File?>(null) }
    var newFileName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<File?>(null) }

    // Player
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
            prepare()
            play()
        }
    }

    var isPlaying by remember { mutableStateOf(true) }
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }

    // Update slider
    LaunchedEffect(isPlaying) {
        while (true) {
            if (player.isPlaying) {
                position = player.currentPosition
                duration = player.duration.coerceAtLeast(1)
            }
            delay(200)
        }
    }

    DisposableEffect(Unit) { onDispose { player.release() } }

    LaunchedEffect(currentFile) {
        player.stop()
        player.setMediaItem(MediaItem.fromUri(Uri.fromFile(currentFile)))
        player.prepare()
        player.play()
        isPlaying = true
    }

    // =========================== UI ===========================

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Audio Player") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState) // <--- FIX SCROLL PORTRAIT/LANDSCAPE
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                currentFile.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            FloatingActionButton(onClick = {
                if (isPlaying) {
                    player.pause()
                    isPlaying = false
                } else {
                    player.play()
                    isPlaying = true
                }
            }) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Slider(
                value = position.toFloat(),
                onValueChange = {
                    position = it.toLong()
                    player.seekTo(position)
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(position))
                Text(formatDuration(duration))
            }

            Spacer(Modifier.height(20.dp))

            Divider()
            Spacer(Modifier.height(12.dp))

            Text(
                "Daftar Rekaman",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(12.dp))

            // ==== LIST FILE ====
            audioFiles.forEach { item ->

                FileCard(
                    data = item,
                    onPlay = { currentFile = item.file },
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

    // ---------------------- DIALOG RENAME ----------------------
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
                    if (file == currentFile) {
                        currentFile = File(context.filesDir, "$newFileName.mp4")
                    }
                    showRenameDialog = null
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) { Text("Batal") }
            }
        )
    }

    // ---------------------- DIALOG DELETE ----------------------
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
                    if (file == currentFile) {
                        if (audioFiles.isNotEmpty()) {
                            currentFile = audioFiles.first().file
                        } else {
                            player.pause()
                        }
                    }
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF7EE)),
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
                    modifier = Modifier.clickable { onEdit() },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    "[Delete]",
                    modifier = Modifier.clickable { onDelete() },
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun loadAudioFiles(context: android.content.Context): List<AudioFileData> {
    return FileManagerUtility.getAllAudioFiles(context).map { file ->
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