package com.devdiaz.sensu.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.devdiaz.sensu.repository.ReminderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class ReminderScheduler
@Inject
constructor(
        @ApplicationContext private val context: Context,
        private val reminderRepository: ReminderRepository
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun schedule() {
        if (!reminderRepository.isReminderEnabled()) {
            cancel()
            return
        }

        val (hour, minute) = reminderRepository.getReminderTime()
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        REMINDER_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

        val calendar =
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

        // If time has already passed today, schedule for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                    )
                } else {
                    // Fallback to inexact if permission not granted (though we should ask for it)
                    // or just use setAndAllowWhileIdle which is less strict but still good
                    alarmManager.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                )
            }
            Log.d("ReminderScheduler", "Alarm scheduled for ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("ReminderScheduler", "Failed to schedule alarm", e)
        }
    }

    fun cancel() {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        REMINDER_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
        alarmManager.cancel(pendingIntent)
        Log.d("ReminderScheduler", "Alarm cancelled")
    }

    companion object {
        const val REMINDER_REQUEST_CODE = 1001
    }
}
