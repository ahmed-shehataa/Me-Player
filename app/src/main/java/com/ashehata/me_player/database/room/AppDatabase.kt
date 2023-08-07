package com.ashehata.me_player.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ashehata.me_player.modules.home.data.local.dao.TracksDao
import com.ashehata.me_player.modules.home.data.model.TrackDataModel

@Database(entities = [TrackDataModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao
}
