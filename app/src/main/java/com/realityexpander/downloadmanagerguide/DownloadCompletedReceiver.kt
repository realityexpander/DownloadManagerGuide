package com.realityexpander.downloadmanagerguide

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking

class DownloadCompletedReceiver: BroadcastReceiver() {

    private lateinit var downloadManager: DownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {
        downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager


        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            val query = DownloadManager.Query().setFilterById(id) //.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL)

            downloadManager.query(query).use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(columnStatusIndex)

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        val columnLocalUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                        val uri = cursor.getString(columnLocalUriIndex)

                        println("Download completed: $uri")
                        runBlocking {
                            downloadResult.emit(DownloadResult(id, uri))
                        }
                    }
                }
            }

            if(id != -1L) {
                println("Download with ID $id finished!")
            }
        }
    }
}