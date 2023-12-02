package com.ashehata.me_player.features.home.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tracks")
data class TrackDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val uri: String,
    val duration: Int,
    val size: Int,
    val wavesList: List<Int>,
    val isFav: Boolean,
    val playingCount: Int,
)
