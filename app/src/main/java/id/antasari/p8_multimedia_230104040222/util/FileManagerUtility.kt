package id.antasari.p8_multimedia_230104040222.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File
import kotlin.math.roundToInt

data class AudioFileData(
    val file: File,
    val durationMs: Long,
    val sizeText: String
)

object FileManagerUtility {

    // =========================== AUDIO ===========================

    /** Ambil seluruh file audio rekaman (.mp4) dari internal storage */
    fun getAllAudioFiles(context: Context): List<File> {
        return context.filesDir
            .listFiles()
            ?.filter {
                it.extension.lowercase() == "mp4" &&
                        it.name.startsWith("audio_")
            }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /** Mengambil durasi audio menggunakan ExoPlayer tanpa memutar */
    fun getAudioDuration(context: Context, file: File): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            val duration = retriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong() ?: 0L
            retriever.release()
            duration
        } catch (e: Exception) {
            0L
        }
    }

    // =========================== VIDEO ===========================

    /** Ambil seluruh file video (.mp4) dari internal storage */
    fun getAllVideoFiles(context: Context): List<File> {
        return context.filesDir
            .listFiles()
            ?.filter {
                it.extension.lowercase() == "mp4" &&
                        it.name.startsWith("video_")
            }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /** Mengambil durasi video menggunakan MediaMetadataRetriever */
    fun getVideoDuration(file: File): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            val duration =
                retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION
                )?.toLong() ?: 0L
            retriever.release()
            duration
        } catch (e: Exception) {
            0L
        }
    }

    // =========================== COMMON ===========================

    /** Convert bytes -> KB / MB */
    fun formatFileSize(bytes: Long): String {
        val kb = bytes / 1024f
        return if (kb > 1024) {
            "${(kb / 1024).roundToInt()} MB"
        } else {
            "${kb.roundToInt()} KB"
        }
    }

    /** Rename file */
    fun renameFile(oldFile: File, newName: String): Boolean {
        val newFile = File(oldFile.parent, newName)
        return oldFile.renameTo(newFile)
    }

    /** Delete file */
    fun deleteFile(file: File): Boolean {
        return file.delete()
    }
}