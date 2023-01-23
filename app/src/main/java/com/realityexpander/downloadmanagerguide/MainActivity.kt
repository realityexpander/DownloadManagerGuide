package com.realityexpander.downloadmanagerguide

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.realityexpander.downloadmanagerguide.ui.theme.DownloadManagerGuideTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


var downloadedUri: String? = null
var downloadUri: MutableStateFlow<String?> = MutableStateFlow<String?>(null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val downloader = AndroidDownloader(this)
        downloader.downloadFile("https://pl-coding.com/wp-content/uploads/2022/04/pic-squared.jpg")

        super.onCreate(savedInstanceState)
        setContent {
            DownloadManagerGuideTheme {

                val uri = downloadUri.asStateFlow().collectAsState()

                DownloadedImage(uri.value)
            }
        }
    }
}

@Composable
fun DownloadedImage(downloadedUri: String?) {

    Text(text = downloadedUri ?: "No image downloaded yet")

    downloadedUri?.let { uri ->
        val context = LocalContext.current
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri.toUri())
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Downloaded Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=32.dp)
                .height(300.dp)
        )
    }
}