package com.devdiaz.sensu.repository

interface ReminderRepository {
    fun getReminderTime(): Pair<Int, Int> // Hour, Minute
    fun saveReminderTime(hour: Int, minute: Int)
    fun isReminderEnabled(): Boolean
    fun setReminderEnabled(enabled: Boolean)
}
