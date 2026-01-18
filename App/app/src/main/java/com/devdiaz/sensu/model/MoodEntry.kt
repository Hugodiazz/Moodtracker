package com.devdiaz.sensu.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devdiaz.sensu.enums.MoodEmotion
import com.devdiaz.sensu.enums.MoodRating

@Entity(tableName = "mood_entries", indices = [Index(value = ["dateString"], unique = true)])
data class MoodEntry(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val rating: MoodRating,
        val emotion: MoodEmotion,
        val timestamp: Long = System.currentTimeMillis(),
        val dateString: String,
        val note: String? = null
)
