package com.devdiaz.sensu.di

import com.devdiaz.sensu.repository.MoodRepository
import com.devdiaz.sensu.repository.MoodRepositoryImpl
import com.devdiaz.sensu.repository.UserStatsRepository
import com.devdiaz.sensu.repository.UserStatsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMoodRepository(moodRepositoryImpl: MoodRepositoryImpl): MoodRepository

    @Binds
    @Singleton
    abstract fun bindUserStatsRepository(
            userStatsRepositoryImpl: UserStatsRepositoryImpl
    ): UserStatsRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
            reminderRepositoryImpl: com.devdiaz.sensu.repository.ReminderRepositoryImpl
    ): com.devdiaz.sensu.repository.ReminderRepository
}
