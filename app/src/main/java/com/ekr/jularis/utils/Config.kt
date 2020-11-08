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

    const val CLIENT_ID = "491460880800-sg6aq3qi4ulr8qj1d36khtsqs18084hb.apps.googleusercontent.com"
    const val CLIENT_SECRET = "MDhit5Dw3_70zpuePK3wQVdE"
}
