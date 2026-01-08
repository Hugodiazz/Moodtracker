package com.devdiaz.sensu.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
        ReminderRepository {

    private val prefs: SharedPreferences =
            context.getSharedPreferences("sensu_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REMINDER_HOUR = "reminder_hour"
        private const val KEY_REMINDER_MINUTE = "reminder_minute"
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"
        private const val DEFAULT_HOUR = 20 // 8 PM
        private const val DEFAULT_MINUTE = 0
    }

    override fun getReminderTime(): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_REMINDER_HOUR, DEFAULT_HOUR)
        val minute = prefs.getInt(KEY_REMINDER_MINUTE, DEFAULT_MINUTE)
        return Pair(hour, minute)
    }

    override fun saveReminderTime(hour: Int, minute: Int) {
        prefs.edit().putInt(KEY_REMINDER_HOUR, hour).putInt(KEY_REMINDER_MINUTE, minute).apply()
    }

    override fun isReminderEnabled(): Boolean {
        return prefs.getBoolean(KEY_REMINDER_ENABLED, false)
    }

    override fun setReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply()
    }
}
