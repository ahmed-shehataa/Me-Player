package com.ashehata.me_player.modules.home.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.ashehata.me_player.modules.home.data.model.TrackDataModel

@Dao
interface TracksDao {

    @Query("SELECT * FROM Tracks")
    fun getAllTracks(): PagingSource<Int, TrackDataModel>

    //TODO change OnConflictStrategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(tracksList: List<TrackDataModel>)

    @Update()
    suspend fun update(trackDataModel: TrackDataModel)

    @Query("DELETE FROM Tracks")
    suspend fun clearAll()

}