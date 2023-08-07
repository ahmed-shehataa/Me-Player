package com.ashehata.me_player.modules.home.data.local.source

import androidx.paging.PagingSource
import com.ashehata.me_player.modules.home.data.model.TrackDataModel

interface TracksLocalDataSource {

    fun getAllTracks(): PagingSource<Int, TrackDataModel>

    suspend fun insertAll(tracksList: List<TrackDataModel>)

    suspend fun update(trackDataModel: TrackDataModel)

    suspend fun clearAllTracks()

}