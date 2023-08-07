package com.ashehata.me_player.modules.home.domain.repository

import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel

interface TracksRepository {

    suspend fun getAllTracks() : List<TrackDomainModel>

    suspend fun getFavouriteTracks() : List<TrackDomainModel>

    suspend fun getMostPlayedTracks() : List<TrackDomainModel>

    suspend fun updateTracks(tracksList: List<TrackDomainModel>)

}