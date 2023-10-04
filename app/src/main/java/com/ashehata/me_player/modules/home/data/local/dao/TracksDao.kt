package com.ashehata.me_player.modules.home.data.local.dao

import androidx.room.*
import com.ashehata.me_player.modules.home.data.model.TrackDataModel

@Dao
interface TracksDao {

    @Query("SELECT * FROM Tracks LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getAllTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Query("SELECT * FROM Tracks LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getFavouriteTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Query("SELECT * FROM Tracks LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getMostPlayedTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracksList: List<TrackDataModel>)

    @Update()
    suspend fun update(trackDataModel: TrackDataModel)

    @Query("DELETE FROM Tracks")
    suspend fun clearAll()

}