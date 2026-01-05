package com.devdiaz.sensu.repository

import com.devdiaz.sensu.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserStatsRepository {
    fun getUserStats(): Flow<UserStats?>
    suspend fun updateUserStats(stats: UserStats)
}
