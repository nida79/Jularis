package com.ekr.jularis.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class DownloadUtils {
    companion object {
        fun downloadLaporan(activity: Activity, urI: String) {
            val downloadManager: DownloadManager =
                activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val url = Uri.parse(urI)
            val request = DownloadManager.Request(url)
            request.setTitle("Laporan Jularis")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "Laporan-Jularis.xlsx"
            )
            downloadManager.enqueue(request)
        }
    }
}