package com.ashehata.me_player.modules.home.data.local.source

import com.ashehata.me_player.modules.home.data.model.TrackDataModel

interface TracksLocalDataSource {


    suspend fun getAllTracks(): List<TrackDataModel>

    suspend fun getAllTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDataModel>

    suspend fun getFavouriteTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDataModel>

    suspend fun getMostPlayedTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDataModel>

    suspend fun insertAll(tracksList: List<TrackDataModel>)

    suspend fun update(trackDataModel: TrackDataModel)

    suspend fun clearAllTracks()

    suspend fun getAllTracksSize(): Int
    suspend fun insert(trackDataModel: TrackDataModel)

}