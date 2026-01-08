package com.devdiaz.sensu.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devdiaz.sensu.MainActivity
import com.devdiaz.sensu.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)

        // Reschedule for the next day
        reminderScheduler.schedule()
    }

    private fun showNotification(context: Context) {
        val channelId = "mood_reminder_channel"

        // Create Notification Channel (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Recordatorio de Estado de Ánimo"
            val descriptionText = "Notificaciones diarias para registrar tu estado de ánimo"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                    NotificationChannel(channelId, name, importance).apply {
                        description = descriptionText
                    }
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create Intent to open the app
        val openAppIntent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigate_to", "scan") // Optional: Deep link logic
                }
        val pendingIntent: PendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        openAppIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

        // Build Notification
        val builder =
                NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(
                                R.drawable.streak_on
                        ) // Use an existing icon for now, ideally 'ic_notification'
                        .setContentTitle("¿Cómo te sientes hoy?")
                        .setContentText("Tómate un momento para registrar tu estado de ánimo.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

        // Show Notification
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1002, builder.build())
            }
        }
    }
}
