package com.ashehata.me_player.modules.home.data.local.source

import androidx.paging.PagingSource
import com.ashehata.me_player.modules.home.data.local.dao.TracksDao
import com.ashehata.me_player.modules.home.data.model.TrackDataModel
import javax.inject.Inject

class TracksLocalDataSourceImpl @Inject constructor(
    private val dao: TracksDao
) : TracksLocalDataSource {
    override fun getAllTracks(): PagingSource<Int, TrackDataModel> {
        return dao.getAllTracks()
    }

    override suspend fun insertAll(tracksList: List<TrackDataModel>) {
        dao.insertAll(tracksList)
    }

    override suspend fun update(trackDataModel: TrackDataModel) {
        dao.update(trackDataModel)
    }

    override suspend fun clearAllTracks() {
        dao.clearAll()
    }


}