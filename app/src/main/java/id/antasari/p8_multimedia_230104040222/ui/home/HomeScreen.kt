package id.antasari.p8_multimedia_230104040222.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.antasari.p8_multimedia_230104040222.R
import id.antasari.p8_multimedia_230104040222.ui.Screen

@Composable
fun HomeScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFF2ECC71), Color(0xFF00A868))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F6))
            .verticalScroll(scrollState)
    ) {

        // ===================== HEADER =====================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(gradient),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(18.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_multimedia),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Multimedia Studio",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))
            }
        }

        // ===================== HERO CARD =====================
        Card(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .height(180.dp)
                .offset(y = (-26).dp),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_multimedia),
                contentDescription = "Hero",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(8.dp))

        // ===================== MENU GRID =====================
        Column(
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {

            // ===== Row 1 =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MenuCard(
                    icon = Icons.Default.Mic,
                    title = "Record Audio",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(Screen.AudioRecorder.route)
                }

                MenuCard(
                    icon = Icons.Default.PlayArrow,
                    title = "Play Audio",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(Screen.AudioPlayer.route)
                }
            }

            Spacer(Modifier.height(14.dp))

            // ===== Row 2 =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                MenuCard(
                    icon = Icons.Default.Videocam,
                    title = "Play Video",
                    modifier = Modifier.weight(1f)
                ) {
                    // ===== AUTO-DETECT VIDEO (FINAL FIX) =====
                    val videos = context.filesDir
                        .listFiles()
                        ?.filter { it.extension.lowercase() == "mp4" }

                    if (!videos.isNullOrEmpty()) {
                        val videoFile = videos.first()
                        val encoded = Uri.encode(videoFile.absolutePath)
                        navController.navigate(
                            Screen.VideoPlayer.passPath(encoded)
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Belum ada video, silakan rekam atau ambil dari gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                MenuCard(
                    icon = Icons.Default.CameraAlt,
                    title = "Camera & Gallery",
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate(Screen.CameraGallery.route)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ===================== FOOTER =====================
        val footerStyle = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Copyright @2025", style = footerStyle)
            Text(
                "Praktikum #8 Menggunakan Multimedia",
                style = footerStyle.copy(fontWeight = FontWeight.Bold)
            )
            Text("Kuliah Mobile Programming S1 Teknologi Informasi", style = footerStyle)
            Text("UIN Antasari Banjarmasin", style = footerStyle)
        }

        Spacer(Modifier.height(20.dp))
    }
}

// ===================== MENU CARD =====================
@Composable
fun MenuCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00C06B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}