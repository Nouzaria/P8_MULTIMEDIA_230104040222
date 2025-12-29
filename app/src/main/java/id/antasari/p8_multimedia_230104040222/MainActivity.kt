package id.antasari.p8_multimedia_230104040222

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import id.antasari.p8_multimedia_230104040222.ui.AppNavHost
import id.antasari.p8_multimedia_230104040222.ui.theme.P8MultimediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P8MultimediaTheme {
                AppNavHost()
            }
        }
    }
}