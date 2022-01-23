package com.example.someproj.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.someproj.R
import com.example.someproj.consts.Const
import javax.inject.Inject

class AlarmReceiver @Inject constructor(): BroadcastReceiver() {

    private lateinit var messToAlarm: String

    override fun onReceive( context: Context?, intent: Intent?) {
        createNotificationChannel(context!!)
        notifyNotification(context, intent?.getStringExtra(Const.MESS).toString())
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Const.CHANNEL_ID,
                Const.SOME_NOTIFY,
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat
                .from(context)
                .createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context, message: String) {
        with(NotificationManagerCompat.from(context)) {

            messToAlarm = when(message) {
                "" -> Const.REALLY_NOTIFY
                else -> message
            }

            val build = NotificationCompat.Builder(context, Const.CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification))
                .setContentText(messToAlarm)
                .setSmallIcon(R.drawable.ic_notif_active)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify(Const.ALARM_TIME_ID, build.build())

        }

    }

}