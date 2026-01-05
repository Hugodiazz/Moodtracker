package com.devdiaz.sensu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey
    val id: Int = 1, // Siempre será 1

    val currentStreak: Int = 0,    // Racha actual
    val longestStreak: Int = 0,    // Racha histórica más larga
    val lastLogDate: String? = null, // Fecha de la última entrada (YYYY-MM-DD)
    val totalEntries: Int = 0      // Contador total (opcional, para logros)
)