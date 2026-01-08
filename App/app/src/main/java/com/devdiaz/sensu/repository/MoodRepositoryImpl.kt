package com.devdiaz.sensu.repository

import com.devdiaz.sensu.data.MoodEntryDao
import com.devdiaz.sensu.data.UserStatsDao
import com.devdiaz.sensu.model.MoodEntry
import com.devdiaz.sensu.model.UserStats
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class MoodRepositoryImpl
@Inject
constructor(private val moodEntryDao: MoodEntryDao, private val userStatsDao: UserStatsDao) :
        MoodRepository {

        override fun getAllMoodEntries(): Flow<List<MoodEntry>> {
                return moodEntryDao.getAllMoodEntries()
        }

        override fun getMoodEntriesByDate(date: String): Flow<List<MoodEntry>> {
                return moodEntryDao.getMoodEntriesByDate(date)
        }

        override fun getMoodEntriesInRange(start: Long, end: Long): Flow<List<MoodEntry>> {
                return moodEntryDao.getMoodEntriesInRange(start, end)
        }

        override fun getMoodCount(): Flow<Int> {
                return moodEntryDao.getMoodCount()
        }

        override fun getUserStats(): Flow<UserStats?> {
                return userStatsDao.getUserStats()
        }

        override suspend fun insertMoodEntry(entry: MoodEntry) =
                withContext(Dispatchers.IO) {
                        // 1. Insert/Upsert Mood Entry
                        moodEntryDao.insert(entry)

                        // 2. Update Streak Logic
                        val currentStats = userStatsDao.getUserStats().firstOrNull() ?: UserStats()
                        val today = LocalDate.now().toString()
                        val lastDate = currentStats.lastLogDate

                        if (lastDate != today) {
                                val yesterday = LocalDate.now().minusDays(1).toString()
                                val newStreak =
                                        if (lastDate == yesterday) {
                                                currentStats.currentStreak + 1
                                        } else {
                                                1
                                        }

                                val longest = maxOf(currentStats.longestStreak, newStreak)

                                val newStats =
                                        currentStats.copy(
                                                currentStreak = newStreak,
                                                longestStreak = longest,
                                                lastLogDate = today,
                                                totalEntries = currentStats.totalEntries + 1
                                        )
                                userStatsDao.insertOrUpdate(newStats)
                        } else {
                                // Already logged today, maybe just increment total entries if we
                                // want to track count strictly?
                                // But requirements say "streak turned on for the day". It's already
                                // on.
                                // We might want to update totalEntries anyway?
                                // Let's just update totalEntries if we want precise count of
                                // interactions,
                                // but typically streak logic cares about unique days.
                                // If we upserted the mood entry (replaced), we shouldn't
                                // necessarily increment interactions unless we track edits.
                                // Plan says "Update streak on mood entry".
                                // Detailed requirement: "si no hace un regisrto en todo el dia e
                                // inicia el dia siguiente la racha vuelve a comenzar desde 1"
                                // This is covered by "else { 1 }".
                        }
                }

        override suspend fun updateMoodEntry(entry: MoodEntry) =
                withContext(Dispatchers.IO) { moodEntryDao.update(entry) }

        override suspend fun deleteMoodEntry(entry: MoodEntry) =
                withContext(Dispatchers.IO) { moodEntryDao.delete(entry) }
}
