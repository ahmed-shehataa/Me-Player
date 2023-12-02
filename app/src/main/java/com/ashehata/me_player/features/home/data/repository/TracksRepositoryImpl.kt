package com.ashehata.me_player.features.home.data.repository

import android.util.Log
import com.ashehata.me_player.features.home.data.local.source.TracksLocalDataSource
import com.ashehata.me_player.features.home.data.mapper.toDomain
import com.ashehata.me_player.features.home.data.mapper.toLocalDb
import com.ashehata.me_player.features.home.domain.model.TrackDomainModel
import com.ashehata.me_player.features.home.domain.repository.TracksRepository
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(private val tracksLocalDataSource: TracksLocalDataSource) :
    TracksRepository {

    override suspend fun getAllTracks(): List<TrackDomainModel> {
        return tracksLocalDataSource.getAllTracks().map { it.toDomain() }
    }

    override suspend fun getAllTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel> {
        val ssd = tracksLocalDataSource.getAllTracks(page, perPage).map { it.toDomain() }
        Log.i("getAllTracks", "getAllTracks: " + ssd.size)
        return tracksLocalDataSource.getAllTracks(page, perPage).map { it.toDomain() }
    }

    override suspend fun getAllTracksSize(): Int {
       return tracksLocalDataSource.getAllTracksSize()
    }


    override suspend fun getFavouriteTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel> {
        return tracksLocalDataSource.getFavouriteTracks(page, perPage)
            .map { it.toDomain() }
    }

    override suspend fun getMostPlayedTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel> {
        return tracksLocalDataSource.getMostPlayedTracks(page, perPage)
            .map { it.toDomain() }
    }

    override suspend fun updateTracks(tracksList: List<TrackDomainModel>) {
        tracksLocalDataSource.insertAll(tracksList.map { it.toLocalDb() })
    }

    override suspend fun updateTrack(trackDomainModel: TrackDomainModel) {
        tracksLocalDataSource.update(trackDomainModel.toLocalDb())
    }

    override suspend fun insertTrack(trackDomainModel: TrackDomainModel) {
        tracksLocalDataSource.insert(trackDomainModel.toLocalDb())
    }
}