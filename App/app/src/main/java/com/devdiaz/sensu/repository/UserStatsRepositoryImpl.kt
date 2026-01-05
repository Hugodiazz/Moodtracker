package com.devdiaz.sensu.repository

import com.devdiaz.sensu.data.UserStatsDao
import com.devdiaz.sensu.model.UserStats
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserStatsRepositoryImpl @Inject constructor(private val userStatsDao: UserStatsDao) :
        UserStatsRepository {

    override fun getUserStats(): Flow<UserStats?> {
        return userStatsDao.getUserStats()
    }

    override suspend fun updateUserStats(stats: UserStats) =
            withContext(Dispatchers.IO) { userStatsDao.insertOrUpdate(stats) }
}
