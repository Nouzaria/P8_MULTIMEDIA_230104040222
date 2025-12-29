package id.antasari.p8_multimedia_230104040222.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AudioRecorder : Screen("audio_recorder")
    object AudioPlayer : Screen("audio_player/{audioPath}") {
        fun passPath(encoded: String): String =
            "audio_player/$encoded"
    }
    object VideoPlayer : Screen("video_player/{videoPath}") {
        fun passPath(encoded: String): String =
            "video_player/$encoded"
    }
    object CameraGallery : Screen("camera_gallery")
}