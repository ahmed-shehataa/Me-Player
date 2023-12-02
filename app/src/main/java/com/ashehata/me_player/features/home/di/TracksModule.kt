package com.ashehata.me_player.features.home.di

import com.ashehata.me_player.database.room.AppDatabase
import com.ashehata.me_player.features.home.data.local.dao.TracksDao
import com.ashehata.me_player.features.home.data.local.source.TracksLocalDataSource
import com.ashehata.me_player.features.home.data.local.source.TracksLocalDataSourceImpl
import com.ashehata.me_player.features.home.data.repository.TracksRepositoryImpl
import com.ashehata.me_player.features.home.domain.repository.TracksRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class TracksModule {

    @ViewModelScoped
    @Binds
    abstract fun bindTracksRepository(tracksRepoImpl: TracksRepositoryImpl): TracksRepository


    @ViewModelScoped
    @Binds
    abstract fun bindTracksLocalDataSource(tracksLocalDataSourceImpl: TracksLocalDataSourceImpl): TracksLocalDataSource

    companion object {
        @Provides
        @ViewModelScoped
        fun provideExhibitorsDao(appDatabase: AppDatabase): TracksDao =
            appDatabase.tracksDao()
    }
}