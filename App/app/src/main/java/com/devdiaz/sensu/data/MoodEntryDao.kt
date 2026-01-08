package com.devdiaz.sensu.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devdiaz.sensu.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entry: MoodEntry)

    @Update suspend fun update(entry: MoodEntry)

    @Delete suspend fun delete(entry: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE dateString = :date ORDER BY timestamp DESC")
    fun getMoodEntriesByDate(date: String): Flow<List<MoodEntry>>

    @Query(
            "SELECT * FROM mood_entries WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC"
    )
    fun getMoodEntriesInRange(start: Long, end: Long): Flow<List<MoodEntry>>

    @Query("SELECT COUNT(DISTINCT dateString) FROM mood_entries") fun getMoodCount(): Flow<Int>
}
