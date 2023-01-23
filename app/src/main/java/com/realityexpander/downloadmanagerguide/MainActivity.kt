package com.realityexpander.downloadmanagerguide

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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


data class DownloadResult(
    val id: Long,
    val uri: String?,
)

var downloadResult: MutableStateFlow<DownloadResult?> = MutableStateFlow(null)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val downloader = AndroidDownloader(this)
        downloader.downloadFile("https://cdn.pixabay.com/photo/2014/07/16/05/18/beach-394503_1280.jpg")

        super.onCreate(savedInstanceState)
        setContent {
            DownloadManagerGuideTheme {

                val downloadState = downloadResult.asStateFlow().collectAsState().value

                Column {
                    DownloadedImage(downloadState)

                    Text("Download ID: ${downloadState?.id}")
                    Text("Downloaded URI: ${downloadState?.uri ?: "No image downloaded yet"}")
                }
            }
        }
    }
}

@Composable
fun DownloadedImage(downloadResult: DownloadResult?) {

    downloadResult?.let { result ->
        val context = LocalContext.current
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, result.uri?.toUri())

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