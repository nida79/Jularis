package com.ekr.jularis.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ekr.jularis.R
import com.ekr.jularis.ui.MainActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException

object NotificationHelper {
    fun displayNotification(context: Context?, title: String?, body: String?) {

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context!!, Config.CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_icon_app)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val mNotificationMgr = NotificationManagerCompat.from(context)
        mNotificationMgr.notify(1, mBuilder.build())
    }
    fun logoutFCM() {
        Thread {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}
