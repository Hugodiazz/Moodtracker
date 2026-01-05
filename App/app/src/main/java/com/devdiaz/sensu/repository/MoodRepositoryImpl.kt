package com.devdiaz.sensu.repository

import com.devdiaz.sensu.data.MoodEntryDao
import com.devdiaz.sensu.model.MoodEntry
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MoodRepositoryImpl @Inject constructor(private val moodEntryDao: MoodEntryDao) :
        MoodRepository {

    override fun getAllMoodEntries(): Flow<List<MoodEntry>> {
        return moodEntryDao.getAllMoodEntries()
    }

    override fun getMoodEntriesByDate(date: String): Flow<List<MoodEntry>> {
        return moodEntryDao.getMoodEntriesByDate(date)
    }

    override suspend fun insertMoodEntry(entry: MoodEntry) =
            withContext(Dispatchers.IO) { moodEntryDao.insert(entry) }

    override suspend fun updateMoodEntry(entry: MoodEntry) =
            withContext(Dispatchers.IO) { moodEntryDao.update(entry) }

    override suspend fun deleteMoodEntry(entry: MoodEntry) =
            withContext(Dispatchers.IO) { moodEntryDao.delete(entry) }
}
