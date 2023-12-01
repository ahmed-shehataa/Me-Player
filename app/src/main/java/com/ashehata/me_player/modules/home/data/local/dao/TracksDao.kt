package com.ashehata.me_player.modules.home.data.local.dao

import androidx.room.*
import com.ashehata.me_player.modules.home.data.model.TrackDataModel

@Dao
interface TracksDao {

    @Query("SELECT COUNT(name) FROM Tracks")
    suspend fun getAllTracksSize(): Int

    @Query("SELECT * FROM Tracks")
    suspend fun getAllTracks(): List<TrackDataModel>

    @Query("SELECT * FROM Tracks LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getAllTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Query("SELECT * FROM Tracks WHERE isFav == 1 LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getFavouriteTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Query("SELECT * FROM Tracks WHERE playingCount > 2 ORDER BY playingCount LIMIT :perPage OFFSET (:page - 1) * :perPage")
    suspend fun getMostPlayedTracks(page: Int, perPage: Int): List<TrackDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(tracksList: List<TrackDataModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trackDataModel: TrackDataModel)

    @Update
    suspend fun update(trackDataModel: TrackDataModel)

    @Query("DELETE FROM Tracks")
    suspend fun clearAll()

}