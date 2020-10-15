package com.ekr.jularis.utils

object Config {
    const val CHANNEL_ID = "Jularis"
    const val CHANNEL_NAME = "Jularis Notif BARU"
    const val CHANNEL_DESC = "Jularis Notifications"

    // global topic to receive app wide push notifications
    const val TOPIC_GLOBAL = "admin"

    // broadcast receiver intent filters
    const val REGISTRATION_COMPLETE = "registrationComplete"
    const val PUSH_NOTIFICATION = "pushNotification"

    // id to handle the notification in the notification tray
    const val NOTIFICATION_ID = 100
    const val NOTIFICATION_ID_BIG_IMAGE = 101
    const val SHARED_PREF = "ah_firebase"
}
