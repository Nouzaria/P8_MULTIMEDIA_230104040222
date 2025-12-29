package id.antasari.p8_multimedia_230104040222.ui

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.antasari.p8_multimedia_230104040222.ui.gallery.CameraGalleryScreen
import id.antasari.p8_multimedia_230104040222.ui.home.HomeScreen
import id.antasari.p8_multimedia_230104040222.ui.player.AudioPlayerScreen
import id.antasari.p8_multimedia_230104040222.ui.recorder.AudioRecorderScreen
import id.antasari.p8_multimedia_230104040222.ui.video.VideoPlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(startDestination: String = Screen.Home.route) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ======================= HOME =======================
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // =================== AUDIO RECORDER ===================
        composable(Screen.AudioRecorder.route) {
            AudioRecorderScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // ==================== AUDIO PLAYER ====================
        composable(
            route = Screen.AudioPlayer.route,
            arguments = listOf(
                navArgument("audioPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val audioPath = Uri.decode(
                backStackEntry.arguments?.getString("audioPath") ?: ""
            )

            AudioPlayerScreen(
                onBack = { navController.popBackStack() },
                audioPath = audioPath
            )
        }

        // ==================== VIDEO PLAYER ====================
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("videoPath") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val videoPath = Uri.decode(
                backStackEntry.arguments?.getString("videoPath") ?: ""
            )

            VideoPlayerScreen(
                onBack = { navController.popBackStack() },
                videoPath = videoPath
            )
        }

        // =================== CAMERA & GALLERY ===================
        composable(Screen.CameraGallery.route) {
            CameraGalleryScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
    }
}