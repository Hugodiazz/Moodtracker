package com.devdiaz.sensu.di

import android.content.Context
import androidx.room.Room
import com.devdiaz.sensu.data.MoodEntryDao
import com.devdiaz.sensu.data.SensuDatabase
import com.devdiaz.sensu.data.UserStatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSensuDatabase(@ApplicationContext context: Context): SensuDatabase {
        return Room.databaseBuilder(context, SensuDatabase::class.java, "sensu_database")
                .fallbackToDestructiveMigration() // Useful for development, be careful in prod
                .build()
    }

    @Provides
    @Singleton
    fun provideMoodEntryDao(database: SensuDatabase): MoodEntryDao {
        return database.moodEntryDao()
    }

    @Provides
    @Singleton
    fun provideUserStatsDao(database: SensuDatabase): UserStatsDao {
        return database.userStatsDao()
    }
}
