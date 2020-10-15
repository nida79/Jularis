package com.ekr.jularis.utils

import android.text.Html
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            NotificationHelper.displayNotification(applicationContext, title, body)
        }
    }
}