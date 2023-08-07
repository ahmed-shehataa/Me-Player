package com.ashehata.me_player.modules.home.data.repository

import androidx.paging.PagingSource
import com.ashehata.me_player.modules.home.data.local.source.TracksLocalDataSource
import com.ashehata.me_player.modules.home.data.mapper.toLocalDb
import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(private val tracksLocalDataSource: TracksLocalDataSource) :
    TracksRepository {
    override suspend fun getAllTracks(): PagingSource<Int, TrackDataModel> {
        return tracksLocalDataSource.getAllTracks()
    }

    override suspend fun getFavouriteTracks(): PagingSource<Int, TrackDataModel> {
        return tracksLocalDataSource.getAllTracks()
    }

    override suspend fun getMostPlayedTracks(): PagingSource<Int, TrackDataModel> {
        return tracksLocalDataSource.getAllTracks()
    }

    override suspend fun updateTracks(tracksList: List<TrackDomainModel>) {
        tracksLocalDataSource.insertAll(tracksList.map { it.toLocalDb() })
    }
}