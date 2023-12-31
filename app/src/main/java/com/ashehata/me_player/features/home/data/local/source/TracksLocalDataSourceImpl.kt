package com.ashehata.me_player.features.home.data.local.source

import android.util.Log
import com.ashehata.me_player.features.home.data.local.dao.TracksDao
import com.ashehata.me_player.features.home.data.model.TrackDataModel
import javax.inject.Inject

class TracksLocalDataSourceImpl @Inject constructor(
    private val dao: TracksDao
) : TracksLocalDataSource {

    override suspend fun getAllTracks(): List<TrackDataModel> {
       return dao.getAllTracks()
    }

    override suspend fun getAllTracks(
        page: Int,
        perPage: Int,
    ): List<TrackDataModel> {
        return dao.getAllTracks(page, perPage)
    }

    override suspend fun getFavouriteTracks(page: Int, perPage: Int): List<TrackDataModel> {
        return dao.getFavouriteTracks(page, perPage)
    }

    override suspend fun getMostPlayedTracks(page: Int, perPage: Int): List<TrackDataModel> {
        return dao.getMostPlayedTracks(page, perPage)
    }

    override suspend fun insertAll(tracksList: List<TrackDataModel>) {
        dao.insertAll(tracksList)
    }

    override suspend fun update(trackDataModel: TrackDataModel) {
        Log.i("TracksLocalData", "update: " + trackDataModel.toString())
        dao.update(trackDataModel)
    }

    override suspend fun clearAllTracks() {
        dao.clearAll()
    }

    override suspend fun getAllTracksSize(): Int {
       return dao.getAllTracksSize()
    }

    override suspend fun insert(trackDataModel: TrackDataModel) {
        dao.insert(trackDataModel)
    }


}