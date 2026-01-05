package com.devdiaz.sensu.data

import androidx.room.*
import com.devdiaz.sensu.model.UserStats
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertOrUpdate(stats: UserStats)

    @Update suspend fun update(stats: UserStats)

    // Al haber solo 1 fila (id=1), basta con un query simple
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1") fun getUserStats(): Flow<UserStats?>
}
