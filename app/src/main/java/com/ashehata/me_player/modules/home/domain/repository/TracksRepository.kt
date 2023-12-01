package com.ashehata.me_player.modules.home.domain.repository

import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel

interface TracksRepository {

    suspend fun getAllTracks(): List<TrackDomainModel>

    suspend fun getAllTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel>

    suspend fun getAllTracksSize(): Int

    suspend fun getFavouriteTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel>

    suspend fun getMostPlayedTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDomainModel>

    suspend fun updateTracks(tracksList: List<TrackDomainModel>)

    suspend fun updateTrack(trackDomainModel: TrackDomainModel)
    suspend fun insertTrack(trackDomainModel: TrackDomainModel)

}