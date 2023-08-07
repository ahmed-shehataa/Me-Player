package com.ashehata.me_player.modules.home.domain.repository

import androidx.paging.PagingSource
import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import com.ashehata.me_player.modules.home.domain.model.TrackDomainModel

interface TracksRepository {

    suspend fun getAllTracks() : PagingSource<Int, TrackDataModel>

    suspend fun getFavouriteTracks() : PagingSource<Int, TrackDataModel>

    suspend fun getMostPlayedTracks() : PagingSource<Int, TrackDataModel>

    suspend fun updateTracks(tracksList: List<TrackDomainModel>)

}