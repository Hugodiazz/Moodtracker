package com.devdiaz.sensu.repository

import com.devdiaz.sensu.model.MoodEntry
import kotlinx.coroutines.flow.Flow

interface MoodRepository {
    fun getAllMoodEntries(): Flow<List<MoodEntry>>
    fun getMoodEntriesByDate(date: String): Flow<List<MoodEntry>>
    fun getMoodEntriesInRange(start: Long, end: Long): Flow<List<MoodEntry>>
    fun getMoodCount(): Flow<Int>
    suspend fun insertMoodEntry(entry: MoodEntry)
    suspend fun updateMoodEntry(entry: MoodEntry)
    suspend fun deleteMoodEntry(entry: MoodEntry)
    fun getUserStats(): Flow<com.devdiaz.sensu.model.UserStats?>
}
