package com.devdiaz.sensu.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devdiaz.sensu.mapper.MoodTypeConverters
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.model.UserStats

@Database(entities = [MoodEntry::class, UserStats::class], version = 1, exportSchema = false)
@TypeConverters(MoodTypeConverters::class)
abstract class SensuDatabase : RoomDatabase() {
    abstract fun moodEntryDao(): MoodEntryDao
    abstract fun userStatsDao(): UserStatsDao
}
