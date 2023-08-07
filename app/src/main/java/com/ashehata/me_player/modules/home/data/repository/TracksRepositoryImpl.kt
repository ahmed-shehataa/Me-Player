package com.ashehata.me_player.modules.home.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.ashehata.me_player.modules.home.data.local.source.TracksLocalDataSource
import com.ashehata.me_player.modules.home.data.mapper.toDomain
import com.ashehata.me_player.modules.home.data.mapper.toLocalDb
import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel
import com.ashehata.me_player.modules.home.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(private val tracksLocalDataSource: TracksLocalDataSource) :
    TracksRepository {

    override suspend fun getAllTracks(): Flow<PagingData<TrackDomainModel>> {
        val tracksFlow = Pager(PagingConfig(pageSize = 50)) {
            tracksLocalDataSource.getAllTracks()
        }.flow

        val tracksFlowMapped = tracksFlow.map {
            it.map {
                Log.i("getAllTracks", it.name)
                it.toDomain()
            }
        }
        return tracksFlowMapped
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